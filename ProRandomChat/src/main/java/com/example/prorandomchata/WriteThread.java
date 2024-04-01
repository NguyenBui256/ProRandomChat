package com.example.prorandomchata; /**
 * Thread này chịu trách nhiệm đọc dữ liệu input từ người dùng và gửi tới server
 * Nó chạy một vòng lặp vô hạn cho đến khi người dùng đánh 'bye' để thoát.
 *
 * @timoday.edu.vn
 */
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class WriteThread extends Thread {

    private Socket socket;
    private ChatClient client;

    private ChatTabController chatTabController;

    public WriteThread(Socket socket, ChatClient client, ChatTabController chatTabController) {
        this.socket = socket;
        this.client = client;
        this.chatTabController = chatTabController;

        try {
            OutputStream output = socket.getOutputStream();

        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}