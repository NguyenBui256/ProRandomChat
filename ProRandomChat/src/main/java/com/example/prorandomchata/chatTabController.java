package com.example.prorandomchata;

import com.example.prorandomchata.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;

public class chatTabController {
    private User sender, receiver;
    private MainController mainController;
    @FXML
    private ImageView receiverAvatar;

    @FXML
    private TextArea textHistory, textTyping;

    @FXML
    private Label receiverName, receiverAge, receiverGender, receiverLocation;

    @FXML
    private Button viewBtn, addFriendBtn, blockBtn, sendBtn;

    public void setUser(User sender, MainController mainController)
    {
        this.mainController = mainController;
//        receiverName.setText(receiver.getUserName());
//        receiverAge.setText(String.valueOf(receiver.getUserAge()));
//        receiverGender.setText(receiver.getUserGender());
//        receiverLocation.setText(receiver.getUserLocation());
//        receiverAvatar.setImage(new Image(new File(receiver.getUserAvatarPath()).toURI().toString()));

        this.sender = sender;
//        this.receiver = receiver;
        textHistory.setEditable(false);
        textTyping.setEditable(true);
    }

    @FXML
    public void view(ActionEvent event) throws IOException {
        mainController.addNewProfileTab(receiver);
        System.out.println("yessir");
    }

}
