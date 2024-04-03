package com.example.prorandomchata;

import com.example.prorandomchata.Controller.IOSystem;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppStarter extends Application {
    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource("loginForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setTitle("Pro Random Chat");
        stage.setScene(scene);
        stage.show();

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