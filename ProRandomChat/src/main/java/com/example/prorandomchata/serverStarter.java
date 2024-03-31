package com.example.prorandomchata;

import java.io.IOException;

public class serverStarter {
    static ChatServer chatServer = new ChatServer(3000);
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        chatServer.execute();
    }

    public static ChatServer getChatServer() {
        return chatServer;
    }
}
