package ProRandomChat;

import ProRandomChat.Model.ChatServer;

import java.io.IOException;

public class ServerStarter {
    protected static ChatServer chatServer;
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        chatServer = new ChatServer(3000);
        chatServer.execute();
    }
}
