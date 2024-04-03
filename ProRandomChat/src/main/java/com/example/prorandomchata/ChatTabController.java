package com.example.prorandomchata;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class ChatTabController implements Serializable{
    protected MainController mainController;
    protected User sender, receiver;
    protected ObjectOutputStream oos;

    @FXML
    protected ImageView receiverAvatar;

    @FXML
    protected TextArea textHistory, textTyping;

    @FXML
    protected Label receiverName, receiverAge, receiverGender, receiverLocation;

    @FXML
    protected Button viewBtn, addFriendBtn, blockBtn, sendBtn, connectBtn, disconnectBtn, nextBtn;

    @FXML
    protected Label coverLayer;

    @FXML
    public void view(ActionEvent event) throws IOException {
        mainController.addNewProfileTab(receiver);
    }

    @FXML
    public void send(ActionEvent event) throws IOException {sendMessage();}

    @FXML
    public void connect(ActionEvent event) throws IOException {
        connectBtn.setVisible(false);
        disconnectBtn.setVisible(true);
        coverLayer.setVisible(false);

        Socket newUserSocket = new Socket("localhost", 3000);
        this.oos = new ObjectOutputStream(newUserSocket.getOutputStream());
        new UserManager(newUserSocket,this, oos).start();
        System.out.println("Connected to the chat server");

        oos.writeObject(new RequestFromUser(sender, "#POST", "#NewUser"));
        oos.flush();

        oos.writeObject(new RequestFromUser(sender, "#GET", "#Partner"));
        oos.flush();
    }

    @FXML
    public void next(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Xác nhận kết nối với người mới?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get()== ButtonType.OK) {
            connectToNewUserFirst();
        }
    }




    @FXML
    public void disconnect(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Xác nhận dừng Random Chat?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get()== ButtonType.OK) {userDisconnect();}
    }

    protected void connectToNewUserFirst() throws IOException {
        textHistory.setText("");
        textTyping.setText("");
        RequestFromUser requestFromUser = new RequestFromUser(sender, "#POST", "#ChangeUserFirst");
        requestFromUser.setPartner(receiver);
        oos.writeObject(requestFromUser);
        oos.flush();
    }

    protected void connectToNewUserLater() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Label label = new Label("Bạn chat của bạn đã rời đi, bạn có muốn chat với người khác không?" +
                "\nNếu ở lại, lịch sử cuộc trò chuyện sẽ được giữ.");
        label.setWrapText(true);
        alert.getDialogPane().setContent(label);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get()== ButtonType.OK) {
            textHistory.setText("");
            textTyping.setText("");
            RequestFromUser requestFromUser = new RequestFromUser(sender, "#POST", "#ChangeUserLater");
            oos.writeObject(requestFromUser);
            oos.flush();
        }
        else if(result.get() == ButtonType.NO)
        {
            System.out.println("Stay here");
        }

    }

    protected void userDisconnect() throws IOException {
        disconnectBtn.setVisible(false);
        connectBtn.setVisible(true);
        coverLayer.setVisible(true);
        nextBtn.setVisible(false);
        textTyping.setText("");
        textHistory.setText("");

        RequestFromUser disconnect = new RequestFromUser(sender, "#POST", "#Disconnect");
        oos.writeObject(disconnect);
        oos.flush();
    }


    protected void sendMessage() throws IOException {
        if(!textTyping.getText().isEmpty())
        {
            String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String text = "["+currentTime+"]" + " [" + sender.getUserName() + "]: " +  textTyping.getText();
            textHistory.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            textHistory.appendText(text + "\n");
            textTyping.setText("");
            textHistory.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            RequestFromUser requestFromUser = new RequestFromUser(sender, "#POST", "#SendMessage");
            requestFromUser.setPartner(receiver);
            requestFromUser.setMessage(text);
            oos.writeObject(requestFromUser);
            oos.flush();
        }
    }

    public void setSender(User sender, MainController mainController) throws IOException {
        this.sender = sender;
        this.mainController = mainController;

        //View settings
        addFriendBtn.setVisible(false);
        viewBtn.setVisible(false);
        nextBtn.setVisible(false);
        textHistory.setEditable(false);
        textTyping.setEditable(true);
        textTyping.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER)
            {
                try {
                    sendMessage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        disconnectBtn.setVisible(false);
        coverLayer.setStyle("-fx-background-color:gray; -fx-opacity:0.3");
        coverLayer.setVisible(true);
        connectBtn.setVisible(true);
    }
    public void setReceiver(User receiver)
    {
        addFriendBtn.setVisible(true);
        viewBtn.setVisible(true);
        nextBtn.setVisible(true);
        this.receiver = receiver;
        receiverName.setText(receiver.getUserName());
        receiverAge.setText(String.valueOf(receiver.getUserAge()));
        receiverGender.setText(receiver.getUserGender());
        receiverLocation.setText(receiver.getUserLocation());
        receiverAvatar.setImage(new Image(new File(receiver.getUserAvatarPath()).toURI().toString()));
    }

    public void unsetReceiver() {
        receiverName.setText("");
        receiverAge.setText("");
        receiverGender.setText("");
        receiverLocation.setText("");
        receiverAvatar.setImage(null);
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

    public void setWaitScreen() {
        coverLayer.setVisible(true);
        coverLayer.setTextAlignment(TextAlignment.JUSTIFY);
        coverLayer.setFont(new Font("Arial", 12));
        coverLayer.setText("Matching...");
    }

    public void unSetWaitScreen() {
        coverLayer.setVisible(false);
        coverLayer.setText("");
    }
}
