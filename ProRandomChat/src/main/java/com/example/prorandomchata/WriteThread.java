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
    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;

    private chatTabController chatTabController;

    public WriteThread(Socket socket, ChatClient client, chatTabController chatTabController) {
        this.socket = socket;
        this.client = client;
        this.chatTabController = chatTabController;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        if(!client.getChatTabController().getTextTyping().getText().equals("###exit###") && !client.getChatTabController().getTextTyping().getText().equals(""))
        {
            String text = "[" + client.getUser().getUserName() + "]: " + chatTabController.getTextTyping().getText();
            chatTabController.getTextHistory().appendText(text + "\n");
            chatTabController.getTextTyping().setText("");
            writer.println(text);
        }
        else if(client.getChatTabController().getTextTyping().equals("###exit###"))
        {
            writer.println("###exit###");
        }

    }
}