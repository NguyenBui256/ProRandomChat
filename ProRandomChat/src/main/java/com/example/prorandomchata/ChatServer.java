package com.example.prorandomchata;

import com.example.prorandomchata.Controller.IOSystem;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer implements Serializable{
    public static int port = 3000;
    //Lưu danh sách các tài khoản người dùng
    public volatile static List<User> users = new ArrayList<>();
    public static List<User> usersRegisted = new ArrayList<>();
    public volatile static Map<String,ObjectOutputStream> userSocketMap = new HashMap<>();

    public ChatServer(int port) throws IOException, ClassNotFoundException {
        this.port = port;
        IOSystem ios = new IOSystem();
        usersRegisted = ios.read("userlist.txt");
    }

    public void execute() {
        try {
            ServerSocket serverSocket = new ServerSocket(3000);
            System.out.println("Chat Server is listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                new ServerManager(socket).start();
            }

        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    /**
     * Lấy danh sách các user đang kết nối
     */
}