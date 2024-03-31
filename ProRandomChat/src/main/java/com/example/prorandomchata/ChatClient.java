package com.example.prorandomchata;

import java.net.*;
import java.io.*;

public class ChatClient implements Serializable{
    private int port;
    private Socket socket;
    private User user;
    private chatTabController chatTabController;

    public ChatClient(User user, int port) {
        this.user = user;
        this.port = ChatServer.port;
    }

    public void execute(chatTabController chatTabController) {
        this.chatTabController = chatTabController;
        try {
            socket = new Socket("localhost", port);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);
            oos.flush();
            System.out.println("Connected to the chat server");

            new ReadThread(socket, this, chatTabController).start();

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public chatTabController getChatTabController() {
        return chatTabController;
    }
}