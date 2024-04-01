package com.example.prorandomchata;

import java.net.*;
import java.io.*;
import java.util.List;

public class ChatClient implements Serializable{
    private int port;
    private Socket socket;
    private User user;
    private ChatTabController chatTabController;

    public List<User> users;

    public ChatClient(User user, ChatTabController chatTabController) {
        this.user = user;
        this.chatTabController = chatTabController;
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

    public ChatTabController getChatTabController() {
        return chatTabController;
    }


    public void setUsers(List<User> users) {
        this.users = users;
    }
}