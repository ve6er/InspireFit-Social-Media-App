/*package com.example.java_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.example.java_project.Jdbc.getConnection;

public class SignUp2Controller{
    @FXML
    private TextField bio;
    @FXML
    private TextField ht;
    @FXML
    private TextField ght;
    @FXML
    private TextField wt;
    @FXML
    private TextField gwt;
    @FXML
    private Button next;
    @FXML
    private Button login;
    @FXML
    private Label text;
    @FXML
    private ImageView profileView;
    @FXML
    private Button uploadButton;
    @FXML
    private Circle profileCircle;

    private int ht1;
    private int ght1;
    private int wt1;
    private int gwt1;
    private String bio1;

    private Image profileImage;
    private ByteArrayInputStream byteArrayInputStream;

    public void backToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) login.getScene().getWindow();
        String login_css = this.getClass().getResource("Login_Styling.css").toExternalForm();
        scene.getStylesheets().add(login_css);
        stage.setScene(scene);
        stage.show();
    }

    public void signUp(ActionEvent event) throws IOException {
        bio1 = bio.getText();
//        Integer ht1, ght1, wt1, gwt1;
        if (ht.getText() != null && !ht.getText().isEmpty()) {
            ht1 = Integer.valueOf(ht.getText());
        } else {
            ht1 = 0;
        }
        if (ght.getText() != null && !ght.getText().isEmpty()) {
            ght1 = Integer.valueOf(ght.getText());
        } else {
            ght1 = 0;
        }
        if (wt.getText() != null && !wt.getText().isEmpty()) {
            wt1 = Integer.valueOf(wt.getText());
        } else {
            wt1 = 0;
        }
        if (gwt.getText() != null && !gwt.getText().isEmpty()) {
            gwt1 = Integer.valueOf(gwt.getText());
        } else {
            gwt1 = 0;
        }
//        profileImage=
        if (bio != null) {
            Jdbc j1 = new Jdbc();
            byteArrayInputStream = uploadEverything(ht1, wt1, bio1,ght1,gwt1);
        }
    }

    private ByteArrayInputStream uploadEverything(int ht1, int wt1, String bio1, int ght1, int gwt1) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Stage stage = (Stage) uploadButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                // Read the selected image file
                FileInputStream inputStream = new FileInputStream(selectedFile);
                FileInputStream inputStream2 = new FileInputStream(selectedFile);
                Image image = new Image(inputStream2);
                profileCircle.setFill(new ImagePattern(image));
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int bytesRead;
                byte[] data = new byte[1024];
                while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, bytesRead);
                }
                buffer.flush();
                byte[] photoBytes = buffer.toByteArray();
                int length = photoBytes.length;
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(photoBytes);
                // Insert the image into the database

                return byteArrayInputStream;
//                insertIntoDatabase(ht1, wt1, bio1, ght1, gwt1, byteArrayInputStream);

                // Optional: Update the UI to display the uploaded image
//                Image image = new Image(inputStream);
//                imageView.setImage(image);
//                loadPosts();
                // Close the input stream
//                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }return null;
    }
/*
    @FXML
    private InputStream uploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Stage stage = (Stage) uploadButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                // Read the selected image file
                FileInputStream inputStream = new FileInputStream(selectedFile);

                // Insert the image into the database
                return inputStream;

                // Optional: Update the UI to display the uploaded image
//                Image image = new Image(inputStream);
//                imageView.setImage(image);

                // Close the input stream
//                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @FXML
    private void insert(ActionEvent event) throws IOException{
        insertIntoDatabase(ht1, wt1, bio1, ght1, gwt1, byteArrayInputStream);
        uploadButton.setVisible(false);
        next.setVisible(false);
        login.setVisible(true);
        text.setVisible(true);
    }


    private void insertIntoDatabase(int ht1, int wt1, String bio1, int ght1, int gwt1, InputStream inputStream) {
        String sql = "INSERT INTO user_details (height, weight, bio, goal_height, goal_weight, profile_photo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ht1);
            stmt.setInt(2, wt1);
            // Set the username
            stmt.setString(3, bio1);

            stmt.setInt(4, ght1);
            stmt.setInt(5, gwt1);

            // Set the image as a blob
            stmt.setBinaryStream(6, inputStream);

            // Execute the query
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}*/

package com.example.java_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.example.java_project.Jdbc.getConnection;

public class SignUp2Controller {
    @FXML
    private TextField bio;
    @FXML
    private TextField ht;
    @FXML
    private TextField ght;
    @FXML
    private TextField wt;
    @FXML
    private TextField gwt;
    @FXML
    private Button next;
    @FXML
    private Button login;
    @FXML
    private Label text;
    @FXML
    private ImageView profileView;
    @FXML
    private Button uploadButton;
    @FXML
    private Circle profileCircle;

    private int ht1;
    private int ght1;
    private int wt1;
    private int gwt1;
    private String bio1;

    private Image profileImage;
    private ByteArrayInputStream byteArrayInputStream;

    public void backToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) login.getScene().getWindow();
        String login_css = this.getClass().getResource("Login_Styling.css").toExternalForm();
        scene.getStylesheets().add(login_css);
        stage.setScene(scene);
        stage.show();
    }

    public void signUp(ActionEvent event) throws IOException {
        bio1 = bio.getText();
        if (ht.getText() != null && !ht.getText().isEmpty()) {
            ht1 = Integer.valueOf(ht.getText());
        } else {
            ht1 = 0;
        }
        if (ght.getText() != null && !ght.getText().isEmpty()) {
            ght1 = Integer.valueOf(ght.getText());
        } else {
            ght1 = 0;
        }
        if (wt.getText() != null && !wt.getText().isEmpty()) {
            wt1 = Integer.valueOf(wt.getText());
        } else {
            wt1 = 0;
        }
        if (gwt.getText() != null && !gwt.getText().isEmpty()) {
            gwt1 = Integer.valueOf(gwt.getText());
        } else {
            gwt1 = 0;
        }
//        profileImage=
        if (bio != null) {
            Jdbc j1 = new Jdbc();
            byteArrayInputStream = uploadImage();


        }
    }

    private ByteArrayInputStream uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", ".png", ".jpg", ".jpeg", ".gif")
        );
        Stage stage = (Stage) uploadButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                // Read the selected image file
                FileInputStream inputStream = new FileInputStream(selectedFile);
                FileInputStream inputStream2 = new FileInputStream(selectedFile);
                Image image = new Image(inputStream2);
                profileCircle.setFill(new ImagePattern(image));
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int bytesRead;
                byte[] data = new byte[1024];
                while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, bytesRead);
                }
                buffer.flush();
                byte[] photoBytes = buffer.toByteArray();
                int length = photoBytes.length;
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(photoBytes);
                // Insert the image into the database

                return byteArrayInputStream;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }return null;
    }


    @FXML
    private void insert(ActionEvent event) throws IOException{
        insertIntoDatabase(ht1, wt1, bio1, ght1, gwt1, byteArrayInputStream);
        uploadButton.setVisible(false);
        next.setVisible(false);
        login.setVisible(true);
        text.setVisible(true);
    }


    private void insertIntoDatabase(int ht1, int wt1, String bio1, int ght1, int gwt1, InputStream inputStream) {
        String sql = "INSERT INTO user_details (height, weight, bio, goal_height, goal_weight, profile_photo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ht1);
            stmt.setInt(2, wt1);
            // Set the username
            stmt.setString(3, bio1);

            stmt.setInt(4, ght1);
            stmt.setInt(5, gwt1);

            // Set the image as a blob
            stmt.setBinaryStream(6, inputStream);

            // Execute the query
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}