package com.example.prorandomchata;

import java.io.*;
import java.net.*;
import java.util.List;

/**
 * Đây là thread làm nhiệm vụ đọc dữ liệu đầu vào từ server và in kết quả ra console.
 * Nó sẽ chạy một vòng lặp vô hạn đến khi client ngắt kết nối tới server
 *
 */
public class ReadThread extends Thread {

    private Socket socket;
    private ObjectInputStream reader;
    private ChatTabController chatTabController;

    public ReadThread(Socket socket, ChatTabController chatTabController) {
        this.socket = socket;
        this.chatTabController = chatTabController;
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
                Object fromServer = reader.readObject();
                if(fromServer instanceof String)
                {
                    if(fromServer != null)  chatTabController.getTextHistory().appendText(fromServer + "\n");
                }
                else if(fromServer instanceof List<?>)
                {
//                    boolean foundPartner = false;
                    System.out.println(((List<?>) fromServer).size());
                    for (User user : (List<User>)fromServer){
                        if(!user.getUserName().equals(chatTabController.getSender().getUserName()) && !user.status)
                        {
//                            foundPartner = true;
                            user.status = true;
//                            chatTabController.setReceiver(user);
                            for (User user2 : (List<User>)fromServer)
                            {
                                if(user2.getUserName().equals(chatTabController.getSender().getUserName()))
                                {
                                    user2.status = true;
                                    break;
                                }
                            }
                            break;
                        }
                    }
//                    if(!foundPartner)
//                    {
//
//                    }
//                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//                    oos.writeObject(fromServer);
//                    oos.flush();
                }
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