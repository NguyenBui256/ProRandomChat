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

    private ObjectOutputStream oos;
    private ChatTabController chatTabController;

    public UserThread(Socket socket, User user, ObjectOutputStream oos) {
        this.socket = socket;
        this.user = user;
        this.oos = oos;
//        this.chatTabController = chatTabController;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            ObjectInputStream reader = new ObjectInputStream(input);
            System.out.println("New user connected: " + user.getUserName() + " => Current users: " + ChatServer.users.size());
            printUsers();
            Object fromServer = reader.readObject();
            do {
                if(fromServer instanceof String)
                {
                    ChatServer.broadcast((String) fromServer, this);
                }
                else if(fromServer instanceof List<?>)
                {
                    ChatServer.setUsers((List<User>) fromServer);
                }
            } while (!fromServer.equals("###exit###"));
//
            ChatServer.removeUser(user, this);
            socket.close();
//
//            String serverMessage = user.getUserName() + " has quitted.";
//            ChatServer.broadcast(serverMessage, this);

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * In ra danh sách các user online.
     */
    void printUsers() {
        if (!ChatServer.users.isEmpty()) {
            List<User> userList = ChatServer.users;
            for(User user : userList)
            {
                System.out.println("Connected user: " + user.getUserName());
            }
        }
        else System.out.println("No other users");
    }

    /**
     * Gửi message tới client.
     */
    void sendMessage(String message) throws IOException {
        oos.writeObject(message);
    }

    public Socket getSocket() {
        return socket;
    }
}