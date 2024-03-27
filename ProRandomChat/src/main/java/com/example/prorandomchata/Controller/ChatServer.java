package com.example.prorandomchata.Controller;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Chương trình Chat Server.
 * Ấn Ctrl + C để kết thúc chương trình.
 *
 */
public class ChatServer {
    private int port;
    //Lưu danh sách các tài khoản người dùng
    private Set<String> userNames = new HashSet<>();
    private Set<UserThread> userThreads = new HashSet<>();

    public ChatServer(int port) {
        this.port = port;
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Chat Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");

                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();

            }

        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        if (args.length < 1) {
//            System.out.println("Syntax: java ChatServer ");
//            System.exit(0);
//        }

        int port = 3000;

        ChatServer server = new ChatServer(port);
        server.execute();
    }

    /**
     * Gửi các message từ 1 người dùng tới các người dùng khác
     */
    void broadcast(String message, UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

    /**
     * Lưu username của client mới được kết nối.
     */
    void addUserName(String userName) {
        userNames.add(userName);
    }

    /**
     * Khi client ngắt kết nối, sẽ bỏ username và UserThread
     */
    void removeUser(String userName, UserThread aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println("User " + userName + " quitted");
        }
    }
    /**
     * Lấy danh sách các user đang kết nối
     */
    Set getUserNames() {
        return this.userNames;
    }

    /**
     * Trả về true nếu có user đã kết nối
     */
    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }
}