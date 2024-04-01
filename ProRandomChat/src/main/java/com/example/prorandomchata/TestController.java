package com.example.prorandomchata;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class TestController {
    private Image userAvatar = new Image(new File("C:\\Users\\ADMIN\\IdeaProjects\\ProRandomChat\\ProRandomChat-A\\src\\main\\resources\\img\\ava.png").toURI().toString());
    @FXML
    private ImageView imgv;

    @FXML
    public void set(ActionEvent event)
    {
        imgv.setImage(userAvatar);
        imgv.setFitWidth(250);
        imgv.setFitHeight(250);
        imgv.setCache(true);
        imgv.setVisible(true);
    }
}
