package com.example.prorandomchata;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.util.List;
import java.util.Set;

public class chatTabController implements Serializable{
    private MainController mainController;

    private User sender, receiver;

    private ChatClient chatClient;

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
    public void send(ActionEvent event)
    {
        new WriteThread(chatClient.getSocket(), chatClient, this).start();
    }

    @FXML
    public void connect(ActionEvent event) throws IOException {
        connectBtn.setVisible(false);
        disconnectBtn.setVisible(true);
        coverLayer.setVisible(false);

        chatClient = new ChatClient(sender, 3000);
        chatClient.execute(this);

        System.out.println(ChatServer.users.size());
        List<User> users = ChatServer.users;
        for(User aUser : users)
        {
            System.out.println(aUser.getUserName());
//            if(ChatServer.map.get(aUser) == 0)
//            {
//                ChatServer.map.put(aUser,1);
//                setReceiver(aUser);
//                break;
//            }
        }
    }

    @FXML
    public void disconnect(ActionEvent event) throws IOException {
        textHistory.setText("");
        textTyping.setText("###exit###");
        new WriteThread(chatClient.getSocket(), chatClient, this).start();
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
        this.receiver = receiver;
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
