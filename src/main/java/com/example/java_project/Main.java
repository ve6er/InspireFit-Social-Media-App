package com.example.java_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
        String login_css = this.getClass().getResource("Login_Styling.css").toExternalForm();
        scene.getStylesheets().add(login_css);
        InputStream stream = new FileInputStream("src/main/resources/com/example/java_project/logo.jpg");
        Image img = new Image(stream);
        stage.getIcons().add(img);
        stage.setTitle("InspireFit");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}