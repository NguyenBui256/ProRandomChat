package com.example.prorandomchata;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Thead này điều khiển kết nối cho mỗi client kết nối tới, để cho phép server có thể điều khiển nhiều client tại cùng thời điểm
 *
 * @timoday.edu.vn
 */
public class UserThread extends Thread {
    private User user;
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;
    private chatTabController chatTabController;

    public UserThread(Socket socket, User user) {
        this.socket = socket;
        this.user = user;
//        this.chatTabController = chatTabController;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            server.addUser(user);
            System.out.println("New user connected: " + user.getUserName() + " => Current users: " + ChatServer.users.size());

            String clientMessage;
            do {
                clientMessage = reader.readLine();
                server.broadcast(clientMessage, this);

            } while (!clientMessage.equals("###exit###"));

            server.removeUser(user, this);
            socket.close();

            String serverMessage = user.getUserName() + " has quitted.";
            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * In ra danh sách các user online.
     */
    void printUsers() {
        if (!ChatServer.users.isEmpty()) {
            Set<User> userList = ChatServer.users;
            for(User user : userList)
            {
                System.out.println(user.getUserName());
            }
        }
    }

    /**
     * Gửi message tới client.
     */
    void sendMessage(String message) {
        writer.println(message);
    }
}