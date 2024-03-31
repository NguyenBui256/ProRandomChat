package com.example.prorandomchata;

import java.io.*;
import java.net.*;

/**
 * Đây là thread làm nhiệm vụ đọc dữ liệu đầu vào từ server và in kết quả ra console.
 * Nó sẽ chạy một vòng lặp vô hạn đến khi client ngắt kết nối tới server
 *
 * @timoday.edu.vn
 */
public class ReadThread extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private ChatClient client;
    private chatTabController chatTabController;

    public ReadThread(Socket socket, ChatClient client, chatTabController chatTabController) {
        this.socket = socket;
        this.client = client;
        this.chatTabController = chatTabController;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String response = reader.readLine();
                if(response != null)  chatTabController.getTextHistory().appendText(response + "\n");
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}