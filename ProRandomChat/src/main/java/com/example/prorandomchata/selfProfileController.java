package com.example.prorandomchata;

import com.example.prorandomchata.Controller.IOSystem;
import com.example.prorandomchata.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class selfProfileController {
    private User user;
    private MainController mainController;
    private IOSystem ios = new IOSystem();
    @FXML
    private Button editBtn, saveBtn, chooseBtn;
    @FXML
    private TextField userName, userLocation, userFullname;
    @FXML
    private Label userAge, userGender, userAvatarPath;
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
        userAvatarPath.setText(user.getUserAvatarPath());

        userAvatar.setImage(new Image(new File(user.getUserAvatarPath()).toURI().toString()));
        userAvatar.setFitHeight(235);
        userAvatar.setFitWidth(235);
        userAvatar.setCache(true);
        userAvatar.setVisible(true);

        setEditableFalse();

    }

    private void setEditableFalse(){
        userName.setEditable(false);
        userFullname.setEditable(false);
        userLocation.setEditable(false);
        userDescription.setEditable(false);
        userAvatarPath.setVisible(false);
        chooseBtn.setVisible(false);
        saveBtn.setVisible(false);
    }

    @FXML
    public void edit(ActionEvent event)
    {
        editBtn.setDisable(true);
        saveBtn.setVisible(true);
        chooseBtn.setVisible(true);
        userAvatarPath.setVisible(true);
        userDescription.setEditable(true);
        userLocation.setEditable(true);
        userName.setEditable(true);
        userFullname.setEditable(true);
    }

    @FXML public void save(ActionEvent event) throws IOException, ClassNotFoundException {
        String tempUserName = userName.getText();
        List<User> users = ios.read("userlist.txt");
        boolean nameInUsed = false;
        for(User userI : users)
        {
            if(!userI.getUserName().equals(this.user.getUserName()) && userI.getUserName().equals(tempUserName))
            {
                nameInUsed = true;
                break;
            }
        }
        if(nameInUsed)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Notification");
            alert.setContentText("Tên đã được sử dụng, vui lòng đặt tên khác!");
            alert.show();
            userName.setText("");
        }
        else
        {
            for(User userI : users)
            {
                if(userI.getUserName().equals(this.user.getUserName()))
                {
                    userI.setUserName(userName.getText());
                    userI.setUserFullname(userFullname.getText());
                    userI.setUserDescription(userDescription.getText());
                    userI.setUserLocation(userLocation.getText());
                    userI.setUserAvatarPath(userAvatarPath.getText());
                    setUser(userI,mainController);
                    mainController.updateSave(userI);
                    break;
                }
            }
            ios.write(users, "userlist.txt");
        }
        editBtn.setDisable(false);
    }

    @FXML public void choose(ActionEvent event)
    {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        if(selectedFile != null)
        {
            userAvatarPath.setText(selectedFile.getAbsolutePath());
        }
    }
}
