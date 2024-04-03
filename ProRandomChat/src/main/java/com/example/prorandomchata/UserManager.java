package com.example.prorandomchata;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Optional;

public class UserManager extends Thread {

    protected Socket socket;
    protected ObjectInputStream reader;
    protected ObjectOutputStream oos;
    protected ChatTabController chatTabController;

    public UserManager(Socket socket, ChatTabController chatTabController, ObjectOutputStream oos) throws IOException {
        this.socket = socket;
        this.chatTabController = chatTabController;
        this.oos = oos;
        InputStream input = socket.getInputStream();
        this.reader = new ObjectInputStream(input);
    }

    public void run() {
    while (true) {
        try {
                ResponseFromServer fromServer = (ResponseFromServer) reader.readObject();
                String responseType = fromServer.getType();
                String responseContent = fromServer.getContent();
                if(responseType.equals("#UPDATE"))
                {
                    if(responseContent.equals("#Matching")) {pendingForPartner(fromServer);}
                    else if(responseContent.equals("#Partner")) {setPartner(fromServer);}
                    else if(responseContent.equals("#Message")) {receiveMessage(fromServer);}
                    else if(responseContent.equals("#PartnerQuit")) {partnerQuit(fromServer);}
                }
}         catch (IOException ex) {
            System.out.println("Error reading from server: " + ex.getMessage());
            ex.printStackTrace();
            break;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

    private void partnerQuit(ResponseFromServer fromServer) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                    try {
                        chatTabController.connectToNewUserLater();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
            }
        });
    }

    private void receiveMessage(ResponseFromServer fromServer) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatTabController.getTextHistory().appendText(fromServer.getObject() + "\n");
            }
        });
    }

    private void pendingForPartner(ResponseFromServer fromServer) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatTabController.unsetReceiver();
                chatTabController.setWaitScreen();
            }
        });
    }

    private void setPartner(ResponseFromServer response) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatTabController.unSetWaitScreen();
                chatTabController.setReceiver((User)response.getObject());
            }
        });
    }
}