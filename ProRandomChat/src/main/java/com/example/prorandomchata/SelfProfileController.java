package com.example.prorandomchata;

import com.example.prorandomchata.Controller.IOSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class SelfProfileController {
    protected User user;
    protected MainController mainController;
    protected ObjectOutputStream oos;
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
    public void setUser(User user, MainController mainController, ObjectOutputStream oos){
        this.user = user;
        this.oos = oos;
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
    @FXML public void choose(ActionEvent event)
    {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        if(selectedFile != null)
        {
            userAvatarPath.setText(selectedFile.getAbsolutePath());
        }
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
        User update = new User(userName.getText(),user.getUserPassword(),userFullname.getText(),user.getUserGender(),
                userLocation.getText(),user.getUserAge());
        update.setUserDescription(userDescription.getText());
        update.setGenderCriteria(user.getGenderCriteria());
        update.setUserAvatarPath(user.getUserAvatarPath());

        RequestFromUser requestFromUser = new RequestFromUser(update, "#PUT", "#SaveProfile");
        requestFromUser.setMessage(user.getUserName());
        oos.writeObject(requestFromUser);
    }

    public void saveSuccess(User user) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setContentText("Lưu thành công!");
        alert.show();
        String tempName = mainController.getSender().getUserName();
        mainController.setUser(user);
        mainController.updateSave(tempName);
        editBtn.setDisable(false);
        setEditableFalse();
    }

    public void saveFail()
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Notification");
        alert.setContentText("Tên đã được sử dụng, vui lòng đặt tên khác!");
        alert.show();
        userName.setText(user.getUserName());
    }


}
