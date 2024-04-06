package com.example.prorandomchata;

import com.example.prorandomchata.Controller.IOSystem;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer implements Serializable{
    public static int port = 3000;
    //Lưu danh sách các tài khoản người dùng
    public static List<String> users = new ArrayList<>(); //tên người dùng đang online.
    public static List<User> usersRegisted = new ArrayList<>(); //danh sách tất cả người dùng đã đăng ký trong dtb.
    public static Map<String,ObjectOutputStream> userOutStreamMap = new HashMap<>(); //Map link đến luồng gửi từ server đến người dùng trong app.
    public static Map<String, User> userMap = new HashMap<>(); //Map link đến người dùng đang online

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
}