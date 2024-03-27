package com.example.prorandomchata.Controller; /**
 * Thread này chịu trách nhiệm đọc dữ liệu input từ người dùng và gửi tới server
 * Nó chạy một vòng lặp vô hạn cho đến khi người dùng đánh 'bye' để thoát.
 *
 * @timoday.edu.vn
 */
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;

    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {

        Scanner console = new Scanner(System.in);

        System.out.println("Enter your name: ");
        String userName = console.nextLine();
        //String userName = console.readLine("\nEnter your name: ");
        client.setUserName(userName);
        writer.println(userName);

        String text;

        do {
            //text = console.readLine("[" + userName + "]: ");
            System.out.println("[" + userName + "]: ");
            text = console.nextLine();
            writer.println(text);

        } while (!text.equals("bye"));

        try {
            socket.close();
        } catch (IOException ex) {

            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}