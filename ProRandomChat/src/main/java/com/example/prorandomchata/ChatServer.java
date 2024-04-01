package com.example.prorandomchata;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer implements Serializable{
    public static int port = 3000;
    //Lưu danh sách các tài khoản người dùng
    public volatile static List<User> users = new ArrayList<>();


    public volatile static Set<UserThread> userThreads = new HashSet<>();
    public volatile static Map<User,Integer> map = new HashMap<>();

    public ChatServer(int port) {
        this.port = port;
    }

    void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Chat Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                new ListenThread(socket).start();
            }

        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Gửi các message từ 1 người dùng tới các người dùng khác - gửi dạng quảng bá
     */
    static void broadcast(String message, UserThread excludeUser) throws IOException {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

    /**
     * Lưu username của client mới được kết nối.
     */
    public static void addUser(User user) {
        users.add(user);
        System.out.println("List size: " + users.size());
    }

    /**
     * Khi client ngắt kết nối, sẽ bỏ username và UserThread
     */
    static void removeUser(User user, UserThread aUser) {
        boolean removed = users.remove(user);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println("The user " + user.getUserName() + " quitted");
        }
    }

    public static void setUsers(List<User> userList) throws IOException {
        users = userList;
        for(UserThread userThread : userThreads)
        {
            ObjectOutputStream oos = new ObjectOutputStream(userThread.getSocket().getOutputStream());
            oos.writeObject(users);
        }
    }
    /**
     * Lấy danh sách các user đang kết nối
     */
}