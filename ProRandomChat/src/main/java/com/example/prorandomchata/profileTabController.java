package com.example.prorandomchata;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class profileTabController {
    private User user;
    private MainController mainController;
    @FXML
    private Button addFriendBtn, sendMessageBtn, blockBtn;
    @FXML
    private TextField userName, userLocation, userFullname;
    @FXML
    private Label userAge, userGender;
    @FXML
    private TextArea userDescription;
    @FXML
    private ImageView userAvatar;
    public void setUser(User user, MainController mainController){
        this.user = user;
        this.mainController = mainController;
        userName.setText(user.getUserName());
        userFullname.setText(user.getUserFullname());
        userAge.setText(String.valueOf(user.getUserAge()));
        userGender.setText(user.getUserGender());
        userLocation.setText(user.getUserLocation());
        userDescription.setText(user.getUserDescription());
        userAvatar.setImage(new Image(new File(user.getUserAvatarPath()).toURI().toString()));

        userName.setEditable(false);
        userFullname.setEditable(false);
        userLocation.setEditable(false);
        userDescription.setEditable(false);

    }
}
