package com.example.prorandomchata;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class ProfileTabController {
    protected User user;
    protected MainController mainController;
    @FXML
    protected Button addFriendBtn, sendMessageBtn, blockBtn;
    @FXML
    protected Label userName, userLocation, userFullname;
    @FXML
    protected Label userAge, userGender;
    @FXML
    protected TextArea userDescription;
    @FXML
    protected ImageView userAvatar;
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
        userDescription.setEditable(false);

    }
}
