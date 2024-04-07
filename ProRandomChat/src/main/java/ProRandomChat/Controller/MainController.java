package ProRandomChat.Controller;

import ProRandomChat.Model.RequestFromUser;
import ProRandomChat.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainController {
    protected User sender;
    protected Stage stage;
    protected Parent root;
    protected ObjectOutputStream oos;
    protected LoginController loginController;
    public SelfProfileController selfProfileController;
    public ChatTabController chatTabController;
    @FXML
    protected Label userName;
    @FXML
    protected ImageView userAvatar;
    @FXML
    protected TabPane mainTabPane;
    @FXML
    protected TreeView<String> treeView;
    @FXML
    public void logout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Xác nhận đăng xuất?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get()== ButtonType.OK) {
            RequestFromUser request = new RequestFromUser(sender, "#POST", "#Logout");
            oos.writeObject(request);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setResizable(false);
            stage.setScene(loginController.getThisScene());
            stage.show();
        }
    }
    @FXML
    public void viewSelfProfile(ActionEvent event) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FMXL/selfProfile.fxml"));
        root = loader.load();

        selfProfileController = loader.getController();
        selfProfileController.setUser(sender,this, oos);

        Tab tab = new Tab();
        tab.setText("Your Profile (" + sender.getUserName() + ")");
        tab.setContent(root);
        checkTabUsing(tab);
    }
    public void addNewProfileTab(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FMXL/profileTab.fxml"));
        root = loader.load();
        ProfileTabController profileTabController = loader.getController();
        profileTabController.setUser(user, this);

        Tab tab = new Tab();
        tab.setText(user.getUserName());
        tab.setContent(root);
        checkTabUsing(tab);
    }
    public void setUser(User user) {
        TreeItem<String> root = new TreeItem<>();
        TreeItem<String> listFriend = new TreeItem<>("Danh sách bạn bè: " + "("+"1"+")");
        TreeItem<String> friend1 = new TreeItem<>("Friend1"); //demo friend
        listFriend.getChildren().add(friend1);

        TreeItem<String> incomingMessage = new TreeItem<>("Thông báo: " + "("+"1"+")");
        TreeItem<String> item1 = new TreeItem<>("Chào mừng người dùng mới!!");
        incomingMessage.getChildren().add(item1);
        root.getChildren().addAll(listFriend,incomingMessage);
        root.setExpanded(false);
        treeView.setRoot(root);
        treeView.setShowRoot(false);


        this.sender = user;
        userName.setText(user.getUserName() + ", " + user.getUserAge());
        userAvatar.setImage(new Image(new File(user.getUserAvatarPath()).toURI().toString()));
        userAvatar.setVisible(true);
    }
    public void addRandomChatTab() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FMXL/chatTab.fxml"));
        root = loader.load();

        chatTabController = loader.getController();
        chatTabController.setSender(sender,this, oos);

        Tab tab = new Tab();
        tab.setText("Pro Random Chat");
        tab.setContent(root);
        tab.setClosable(false);
        mainTabPane.getTabs().add(tab);
    }
    public void updateSave(String tempName) throws IOException {
        mainTabPane.getTabs().forEach(tabI -> {
            if(tabI.getText().equals("Your Profile " + "("+tempName+")")){
                tabI.setText("Your Profile " + "("+sender.getUserName()+")");
            }
        });
//        setUser(user);
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
    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }
    public User getSender() {
        return sender;
    }
    public SelfProfileController getSelfProfileController() {return selfProfileController;}
}
