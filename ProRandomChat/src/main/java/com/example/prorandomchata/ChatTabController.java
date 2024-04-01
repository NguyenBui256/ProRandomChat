package com.example.prorandomchata;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ChatTabController implements Serializable{
    private MainController mainController;

    private Socket socket;

    private User sender, receiver;

    private ChatClient chatClient;

    private ObjectOutputStream oos;

    @FXML
    private ImageView receiverAvatar;

    @FXML
    private TextArea textHistory, textTyping;

    @FXML
    private Label receiverName, receiverAge, receiverGender, receiverLocation;

    @FXML
    private Button viewBtn, addFriendBtn, blockBtn, sendBtn, connectBtn, disconnectBtn;

    @FXML
    private Label coverLayer;

    @FXML
    public void view(ActionEvent event) throws IOException {
        mainController.addNewProfileTab(receiver);
    }

    @FXML
    public void send(ActionEvent event) throws IOException {
        if(!textTyping.getText().equals("###exit###") && !textTyping.getText().isEmpty())
        {
            String text = "[" + sender.getUserName() + "]: " + textTyping.getText();
            textHistory.appendText(text + "\n");
            textTyping.setText("");
            oos.writeObject(text);
            oos.flush();
        }
        else if(textTyping.getText().equals("###exit###"))
        {
            oos.writeObject("###exit###");
            oos.flush();
        }
    }

    @FXML
    public void connect(ActionEvent event) throws IOException {
        connectBtn.setVisible(false);
        disconnectBtn.setVisible(true);
        coverLayer.setVisible(false);

        chatClient = new ChatClient(sender,this);
        socket = new Socket("localhost", 3000);
        oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(sender);
        oos.flush();

        new ReadThread(socket,this).start();
        System.out.println("Connected to the chat server");
    }

    @FXML
    public void disconnect(ActionEvent event) throws IOException {
        textHistory.setText("");
        textTyping.setText("###exit###");
//        new WriteThread(chatClient.getSocket(), chatClient, this).start();
        textTyping.setText("");
        disconnectBtn.setVisible(false);
        connectBtn.setVisible(true);
        coverLayer.setVisible(true);
    }

    public void setSender(User sender, MainController mainController)
    {
        this.mainController = mainController;
        this.sender = sender;
        textHistory.setEditable(false);
        textTyping.setEditable(true);
        disconnectBtn.setVisible(false);

        coverLayer.setStyle("-fx-background-color:gray; -fx-opacity:0.3");
        coverLayer.setVisible(true);
        connectBtn.setVisible(true);
    }
    public void setReceiver(User receiver)
    {
//        this.receiver = receiver;
        receiverName.setText(receiver.getUserName());
        receiverAge.setText(String.valueOf(receiver.getUserAge()));
        receiverGender.setText(receiver.getUserGender());
        receiverLocation.setText(receiver.getUserLocation());
        receiverAvatar.setImage(new Image(new File(receiver.getUserAvatarPath()).toURI().toString()));
    }

    public TextArea getTextHistory() {
        return textHistory;
    }

    public TextArea getTextTyping() {
        return textTyping;
    }

    public Button getSendBtn() {
        return sendBtn;
    }

    public User getSender() {
        return sender;
    }
}
