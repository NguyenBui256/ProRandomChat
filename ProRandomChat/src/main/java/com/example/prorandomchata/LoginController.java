package com.example.prorandomchata;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.Period;

public class LoginController {
    protected Stage stage;
    protected Scene scene;
    protected Parent root;
    protected ObjectOutputStream oos;
    protected MainController mainController;

    @FXML
    protected String gender = "";
    protected Scene thisScene;
    @FXML
    protected Label loginLabel, userRegLabel, userRegPassLabel, userRegLocationLabel
            , userRegBirthday, userRegGender, userFullNameLabel, regLabel;
    @FXML
    protected ImageView userImage, userPassImage;
    @FXML
    protected TextField loginUsername, regisUsername, regisLocation, regisFullname;
    @FXML
    protected PasswordField loginPassword, regisPassword;
    @FXML
    protected RadioButton male, female;
    @FXML
    protected DatePicker regisDOB;
    @FXML
    protected Button loginBtn, signUpBtn, loginShowBtn, regShowBtn;
    @FXML
    public void show(ActionEvent event){
        userRegLabel.setVisible(!userRegLabel.isVisible());
        userRegPassLabel.setVisible(!userRegPassLabel.isVisible());
        userRegLocationLabel.setVisible(!userRegLocationLabel.isVisible());
        userFullNameLabel.setVisible(!userFullNameLabel.isVisible());
        userRegBirthday.setVisible(!userRegBirthday.isVisible());
        userRegGender.setVisible(!userRegGender.isVisible());
        regShowBtn.setVisible(!regShowBtn.isVisible());
        regisUsername.setVisible(!regisUsername.isVisible());
        regisPassword.setVisible(!regisPassword.isVisible());
        regisFullname.setVisible(!regisFullname.isVisible());
        regisLocation.setVisible(!regisLocation.isVisible());
        regisDOB.setVisible(!regisDOB.isVisible());
        regLabel.setVisible(!regLabel.isVisible());
        signUpBtn.setVisible(!signUpBtn.isVisible());

        loginLabel.setVisible(!loginLabel.isVisible());
        loginUsername.setVisible(!loginUsername.isVisible());
        loginPassword.setVisible(!loginPassword.isVisible());
        loginBtn.setVisible(!loginBtn.isVisible());
        userImage.setVisible(!userImage.isVisible());
        male.setVisible(!male.isVisible());
        female.setVisible(!female.isVisible());
        userPassImage.setVisible(!userPassImage.isVisible());
        loginShowBtn.setVisible(!loginShowBtn.isVisible());

    }
    @FXML
    public void signUp(ActionEvent event) throws IOException, ClassNotFoundException {
        if(regisUsername.getText().isEmpty())
        {
            regisUsername.setText("Hãy điền tên tài khoản!");
            return;
        }
        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.parse(regisDOB.getValue().toString());
        int userAge = Period.between(birthday, today).getYears();
        User newUser = new User(regisUsername.getText(), regisPassword.getText(), regisFullname.getText(), gender, regisLocation.getText(), userAge);
        RequestFromUser request = new RequestFromUser(newUser, "#POST", "#Signup");
        oos.writeObject(request);
        oos.flush();

        regisUsername.setText("");
        regisPassword.setText("");
        regisLocation.setText("");
        regisFullname.setText("");
        regisDOB.getEditor().clear();
        male.setSelected(false);
        female.setSelected(false);
    }
    @FXML
    public void login(ActionEvent event) throws IOException, ClassNotFoundException {
        if(loginUsername.getText().isEmpty())
        {
            loginUsername.setText("Hãy điền tên đăng nhập!");
            return;
        }
        User newUser = new User("","","","","",0);
        newUser.setUserName(loginUsername.getText());
        newUser.setUserPassword(loginPassword.getText());
        RequestFromUser requestFromUser = new RequestFromUser(newUser, "#POST", "#Login");
        oos.writeObject(requestFromUser);
    }

    public void genderSelect(ActionEvent event) {
        if (male.isSelected()) gender = "Male";
        if (female.isSelected()) gender = "Female";
    }

    public void loginSucess(User user) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setContentText("Success");
        alert.show();

        loginUsername.setText("");
        loginPassword.setText("");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainStage.fxml"));
        root = loader.load();
        mainController = loader.getController();
        mainController.setLoginController(this);
        mainController.setOos(oos);
        mainController.setUser(user);
        mainController.addRandomChatTab();

        stage = (Stage) loginBtn.getScene().getWindow();
        stage.setResizable(false);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void loginFail() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Notification");
        alert.setContentText("Sai tên đăng nhập / mật khẩu");
        alert.show();
        loginPassword.setText("");
    }

    public void setOos(ObjectOutputStream oos) {this.oos = oos;}

    public void signUpSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Noitification");
        alert.setContentText("Đăng kí thành công!");
        alert.show();
    }

    public void signUpFail() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("WARNING");
        alert.setContentText("Tên tài khoản đã được sử dụng. Vui lòng sử dụng tên khác");
        alert.show();
    }

    public MainController getMainController() {return mainController;}

    public Scene getThisScene() {return thisScene;}

    public void setThisScene(Scene thisScene) {this.thisScene = thisScene;}
}
