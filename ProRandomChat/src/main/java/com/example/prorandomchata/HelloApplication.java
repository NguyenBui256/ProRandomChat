package com.example.prorandomchata;

import com.example.prorandomchata.Controller.IOSystem;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loginForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LoginController loginController = fxmlLoader.getController();
        stage.setTitle("Pro Random Chat");
        stage.setScene(scene);
        stage.show();

//        IOSystem ios = new IOSystem();
//        ios.clearData("userlist.txt");
//        User user = new User("btvn", "123", "Bui The Vinh Nguyen", "male", "hanoi", 19);
//        ChatServer.addUser(user);
//        System.out.println(ChatServer.users.size());
//        List<User> list = new ArrayList<>();
//        list.add(user);
//        ios.write(list,"userlist.txt");
//        System.out.println("Ok");
//        List<User> list = new ArrayList<>();
//        try{
//            list = ios.read("userlist.txt");
//            for(User user : list) System.out.println(user.getUserName());
//        }
//        catch (IOException e) {System.out.println("LOL");}
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        launch();
    }
}