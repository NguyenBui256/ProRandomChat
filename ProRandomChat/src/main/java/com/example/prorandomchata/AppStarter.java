package com.example.prorandomchata;

import com.example.prorandomchata.Controller.IOSystem;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class AppStarter extends Application {
    protected Socket newUserSocket;
    protected ObjectOutputStream oos;
    protected UserManager userManager;
    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource("loginForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LoginController loginController = fxmlLoader.getController();
        loginController.loginShowBtn.setBackground(null);
        loginController.regShowBtn.setBackground(null);
        loginController.setThisScene(scene);

        newUserSocket = new Socket("localhost", 3000);
        oos = new ObjectOutputStream(newUserSocket.getOutputStream());
        userManager = new UserManager(newUserSocket, oos);
        userManager.setLoginController(loginController);
        userManager.start();
        System.out.println("Connected to server");

        stage.getIcons().add(new Image(new File("src/main/resources/img/logo.png").toURI().toString()));
        stage.setResizable(false);
        stage.setTitle("Pro Random Chat");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event ->{
            RequestFromUser request = new RequestFromUser(null, "#POST", "#KillApp");;
            if(loginController.getMainController() != null && loginController.getMainController().getSender() != null){
                request.setUser(loginController.getMainController().getSender());
            }
            try {
                oos.writeObject(request);
                userManager.interrupt();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
//        IOSystem ios = new IOSystem();
//        ios.clearData("userlist.txt");
//        User user = new User("btvn", "123", "Bui The Vinh Nguyen", "Male", "hanoi", 19);
//        User user2 = new User("a", "1", "BTVN", "Male", "hanoi", 20);
//        List<User> list = new ArrayList<>();
//        list.add(user);
//        list.add(user2);
//        ios.write(list,"userlist.txt");
//        System.out.println("Ok");
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        launch();
    }
}