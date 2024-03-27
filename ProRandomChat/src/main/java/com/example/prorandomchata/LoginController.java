package com.example.prorandomchata;

import com.example.prorandomchata.Controller.IOSystem;
import com.example.prorandomchata.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.abs;

public class LoginController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    IOSystem ios = new IOSystem();
    @FXML
    private TextField loginUsername, regisUsername, regisLocation, regisFullname;
    @FXML
    private PasswordField loginPassword, regisPassword;
    @FXML
    private RadioButton male, female;
    @FXML
    private DatePicker regisDOB;
    @FXML
    private String gender = "";
    private int userAge;

    public void genderSelect(ActionEvent event) {
        if (male.isSelected()) gender = "Male";
        if (female.isSelected()) gender = "Female";
    }

    public void signUp(ActionEvent event) throws IOException, ClassNotFoundException {

        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.parse(regisDOB.getValue().toString());
        userAge = Period.between(birthday, today).getYears();

        checkAccountUsed();
    }

    public void checkAccountUsed() throws IOException, ClassNotFoundException {
        List<User> userAcounts = ios.read("userlist.txt");
        boolean userIsAlreadyUsed = false;
        User newUser = null;
        for (User user : userAcounts) {
            if (user.getUserName().equals(regisUsername.getText())) {
                userIsAlreadyUsed = true;
                break;
            }
        }
        if (!userIsAlreadyUsed) {
            newUser = new User(regisUsername.getText(), regisPassword.getText(), regisFullname.getText(),  gender, regisLocation.getText(), userAge);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Noitification");
            alert.setContentText("Đăng kí thành công!");
            alert.show();
            userAcounts.add(newUser);
            ios.write(userAcounts, "userlist.txt");
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setContentText("Tên tài khoản đã được sử dụng. Vui lòng sử dụng tên khác");
            alert.show();
        }
        regisUsername.setText("");
        regisPassword.setText("");
        regisLocation.setText("");
        regisFullname.setText("");
        regisDOB.getEditor().clear();
        male.setSelected(false);
        female.setSelected(false);
    }

    public void login(ActionEvent event) throws IOException, ClassNotFoundException {
        List<User> userAcounts = ios.read("userlist.txt");
        boolean userIsFound = false;
        User userClient = null;
        for(User user : userAcounts)
        {
            if(user.getUserName().equals(loginUsername.getText()) && user.getUserPassword().equals(loginPassword.getText()))
            {
                userIsFound = true;
                userClient = user;
                break;
            }
        }
        if(userIsFound)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setContentText("Success");
            alert.show();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainStage.fxml"));
            root = loader.load();

            MainController mainController = loader.getController();
            mainController.setUser(userClient);

            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Notification");
            alert.setContentText("Sai tên đăng nhập / mật khẩu");
            alert.show();
            loginPassword.setText("");
        }
    }
}
