package com.example.prorandomchata;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer implements Serializable{
    public static int port = 3000;
    //Lưu danh sách các tài khoản người dùng
    public static List<User> users = new ArrayList<>();
    public static Set<UserThread> userThreads = new HashSet<>();

    public static Map<User,Integer> map = new HashMap<>();

    public static String name = "";

    public ChatServer(int port) {
        this.port = port;
    }

    static void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Chat Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
//                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//                oos.writeObject(this);
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User user = (User) ois.readObject();
                name = user.getUserName();
                System.out.println("New user connected");

                users.add(user);
                UserThread newUser = new UserThread(socket, user);
                userThreads.add(newUser);
                map.put(user,0);
                newUser.start();
            }

        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gửi các message từ 1 người dùng tới các người dùng khác - gửi dạng quảng bá
     */
    static void broadcast(String message, UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

    /**
     * Lưu username của client mới được kết nối.
     */
    static void addUser(User user) {
        users.add(user);
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
    /**
     * Lấy danh sách các user đang kết nối
     */
}