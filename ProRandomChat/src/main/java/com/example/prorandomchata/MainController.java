package com.example.prorandomchata;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainController {
    private User sender, receiver;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private ChatServer server;

    @FXML
    private Label userName;
    @FXML
    private ImageView userAvatar;
    @FXML
    private TabPane mainTabPane;
    @FXML
    public void logout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Xác nhận đăng xuất?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get()== ButtonType.OK) {
            root = FXMLLoader.load((HelloApplication.class.getResource("loginForm.fxml")));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
    @FXML
    public void viewSelfProfile(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("selfProfile.fxml"));
        root = loader.load();

        SelfProfileController selfProfileController = loader.getController();
        selfProfileController.setUser(sender,this);

        Tab tab = new Tab();
        tab.setText("Your Profile (" + sender.getUserName() + ")");
        tab.setContent(root);

        checkTabUsing(tab);

    }

    public void setUser(User user) throws IOException {
        this.sender = user;
        userName.setText(user.getUserName());
        userAvatar.setImage(new Image(new File(user.getUserAvatarPath()).toURI().toString()));
        userAvatar.setCache(true);
        userAvatar.setVisible(true);

//        addRandomChatTab();
//        userAvatar.setFitHeight(55);
//        userAvatar.setFitWidth(55);
    }

    public void addRandomChatTab() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chatTab.fxml"));
        root = loader.load();

        ChatTabController chatTabController = loader.getController();
        chatTabController.setSender(this.sender,this);

        Tab tab = new Tab();
        tab.setText("Pro Random Chat");
        tab.setContent(root);
        mainTabPane.getTabs().add(tab);
        mainTabPane.getSelectionModel().selectLast();
    }

    public void updateSave(User user) throws IOException {

        mainTabPane.getTabs().forEach(tabI -> {
            if(tabI.getText().equals(userName.getText())){
                tabI.setText(user.getUserName());
            }
        });
        userName.setText(user.getUserName());
        userAvatar.setImage(new Image(new File(user.getUserAvatarPath()).toURI().toString()));
    }

    public void checkTabUsing(Tab tab){
        AtomicBoolean inUsed = new AtomicBoolean(false);
        mainTabPane.getTabs().forEach((tabI)->{
            if(tabI.getText().equals(tab.getText()))
            {
                mainTabPane.getSelectionModel().select(tabI);
                inUsed.set(true);
            }
        });
        if(!inUsed.get())
        {
            mainTabPane.getTabs().add(tab);
            mainTabPane.getSelectionModel().selectLast();
        }
    }

    public void addNewProfileTab(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("profileTab.fxml"));
        root = loader.load();

        ProfileTabController profileTabController = loader.getController();
        profileTabController.setUser(user, this);

        Tab tab = new Tab();
        tab.setText(user.getUserName());
        tab.setContent(root);
        mainTabPane.getTabs().add(tab);
        mainTabPane.getSelectionModel().selectLast();
    }


//    public ChatServer getServer() {
//        return server;
//    }

//    public void setServer(ChatServer server) {
//        this.server = server;
//    }
}
