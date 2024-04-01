package com.example.prorandomchata;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Đây là thread làm nhiệm vụ đọc dữ liệu đầu vào từ server và in kết quả ra console.
 * Nó sẽ chạy một vòng lặp vô hạn đến khi client ngắt kết nối tới server
 *
 */
public class ListenThread extends Thread {
    private ObjectInputStream reader;
    private Socket socket;

    private UserThread userThread;

    public ListenThread(Socket socket) {
        this.socket = socket;
        try {
            InputStream input = socket.getInputStream();
            reader = new ObjectInputStream(input);
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
//                reader.getObjectInputFilter().checkInput();
                Object object = reader.readObject();
                if(object instanceof User)
                {
                    ChatServer.addUser((User) object);
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(ChatServer.users);
                    oos.flush();
                    System.out.println("Da them user");
                    ChatServer.map.put((User) object,0);
                    userThread = new UserThread(socket, (User) object, oos);
                    ChatServer.userThreads.add(userThread);
                    userThread.start();
                    System.out.println("Da them User Thread");
                }
                else if(object instanceof List<?>) {
                    ChatServer.setUsers((List<User>) object);
                }
                else if(object instanceof String)
                {
                    System.out.println((String)object);
                    ChatServer.broadcast((String)object,userThread);
                }
                else System.out.println("No no no");
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}