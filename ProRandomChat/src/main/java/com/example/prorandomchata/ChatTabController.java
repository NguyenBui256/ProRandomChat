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
    protected boolean partnerHasLeft;

    @FXML
    protected RadioButton onlyMale, onlyFemale, allGender;

    @FXML
    protected ImageView receiverAvatar;

    @FXML
    protected TextArea textHistory, textTyping;

    @FXML
    protected Label receiverName, receiverAge, receiverGender, receiverLocation;

    @FXML
    protected Button viewBtn, addFriendBtn, blockBtn, sendBtn, connectBtn, disconnectBtn, nextBtn;
    @FXML
    protected TitledPane settings;

    @FXML
    protected Label coverLayer;

    @FXML
    public void view(ActionEvent event) throws IOException {mainController.addNewProfileTab(receiver);}
    @FXML
    public void send(ActionEvent event) throws IOException {sendMessage();}

    @FXML
    public void connect(ActionEvent event) throws IOException {
        connectBtn.setVisible(false);
        coverLayer.setVisible(false);
        disconnectBtn.setVisible(true);
        settings.setExpanded(false);
//        Socket newUserSocket = new Socket("localhost", 3000);
//        this.oos = new ObjectOutputStream(newUserSocket.getOutputStream());
//        new UserManager(newUserSocket, oos).start();
//        System.out.println("Connected to the chat server");
//
        oos.writeObject(new RequestFromUser(sender, "#PUT", "#UserOnline"));
        oos.flush();
        oos.writeObject(new RequestFromUser(sender, "#GET", "#NewPartner"));
        oos.flush();
    }

    @FXML
    public void next(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Xác nhận kết nối với người mới?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get()== ButtonType.OK) {
            if(partnerHasLeft){
                RequestFromUser requestFromUser = new RequestFromUser(sender, "#PUT", "#ChangeUserLater");
                oos.writeObject(requestFromUser);
                oos.flush();
            }
            else connectToNewUserFirst();
            nextBtn.setDisable(true);
        }
    }
    @FXML
    public void setOnlyMale(ActionEvent event) throws IOException {changeGenderCriteria("Male");}
    @FXML
    public void setOnlyFemale(ActionEvent event) throws IOException {changeGenderCriteria("Female");}
    @FXML
    public void setAllGender(ActionEvent event) throws IOException {changeGenderCriteria("All");}

    public void changeGenderCriteria(String gender) throws IOException {
        sender.setGenderCriteria(gender);
        RequestFromUser request = new RequestFromUser(sender, "#PUT", "#ChangeGenderCriteria");
        System.out.println(sender.genderCriteria);
        request.setMessage(gender);
        settings.setExpanded(false);

        oos.writeObject(request);
        oos.flush();

        if(coverLayer.isVisible()) {oos.writeObject(new RequestFromUser(sender, "#GET", "#NewPartner"));}
    }

    public void setSender(User sender, MainController mainController, ObjectOutputStream oos) throws IOException {
        this.sender = sender;
        this.mainController = mainController;
        this.oos = oos;

        //View settings
        settings.setVisible(true);
        allGender.setSelected(true);
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
        coverLayer.setStyle("-fx-background-color:gray; -fx-opacity:0.5");
        coverLayer.setVisible(true);
        connectBtn.setVisible(true);
    }


//    @FXML
//    public void block(ActionEvent event) throws IOException {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setContentText("Bạn có muốn chặn người này?");
//        Optional<ButtonType> result = alert.showAndWait();
//        if(result.get()== ButtonType.OK) {blockPartner();}
//    }

//    private void blockPartner() throws IOException {
//        RequestFromUser requestFromUser = new RequestFromUser(sender,"#POST", "#Block");
//        requestFromUser.setPartner(receiver);
//        oos.writeObject(requestFromUser);
//        oos.flush();
//    }


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
        RequestFromUser requestFromUser = new RequestFromUser(sender, "#PUT", "#ChangeUserFirst");
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
            RequestFromUser requestFromUser = new RequestFromUser(sender, "#PUT", "#ChangeUserLater");
            oos.writeObject(requestFromUser);
            oos.flush();

            if(coverLayer.isVisible())
            {
                viewBtn.setVisible(false);
                addFriendBtn.setVisible(false);
            }
        }
        else{
            partnerHasLeft = true;
            textTyping.setEditable(false);
        }
    }

    protected void userDisconnect() throws IOException {
        unsetReceiver();
        unSetWaitScreen();
        disconnectBtn.setVisible(false);
        addFriendBtn.setVisible(false);
        viewBtn.setVisible(false);
        connectBtn.setVisible(true);
        coverLayer.setVisible(true);
        nextBtn.setVisible(false);
        textTyping.setText("");
        textHistory.setText("");

        RequestFromUser disconnectRequest = new RequestFromUser(sender, "#POST", "#Disconnect");
        disconnectRequest.setPartner(receiver);
        oos.writeObject(disconnectRequest);
        oos.flush();
    }


    protected void sendMessage() throws IOException {
        if(!textTyping.getText().isEmpty())
        {
            String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String text = "["+currentTime+"]" + " [" + sender.getUserName() + "]: " +  textTyping.getText();
            textHistory.appendText(text);
            textTyping.setText("");
            RequestFromUser requestFromUser = new RequestFromUser(sender, "#POST", "#SendMessage");
            requestFromUser.setPartner(receiver);
            requestFromUser.setMessage(text);
            oos.writeObject(requestFromUser);
            oos.flush();
        }
    }

    public void setReceiver(User receiver)
    {
        addFriendBtn.setVisible(true);
        viewBtn.setVisible(true);
        nextBtn.setVisible(true);
        nextBtn.setDisable(false);
        partnerHasLeft = false;
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


    public void setWaitScreen() {
        coverLayer.setVisible(true);
    }

    public void unSetWaitScreen() {
        coverLayer.setVisible(false);
        coverLayer.setText("");
    }
}
