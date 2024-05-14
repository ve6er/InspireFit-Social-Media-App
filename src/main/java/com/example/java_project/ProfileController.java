package com.example.java_project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private ImageView profileImageView;
    @FXML
    private Label bioTextArea;
    @FXML
    private Label usernameTextField;
    @FXML
    private Button loadButton;
    @FXML
    private Circle realProfileImage;
    @FXML
    private Label usernameLabel;
    @FXML
    private VBox vBox1;
    @FXML
    private ListView<Post> postsListView;
    @FXML
    private ImageView homeButton;
    @FXML
    private ImageView settings;
    String username;
    private String bio;
    private int userWeight;
    private int userHeight;
    private int goal_weight;
    private int goal_height;

    FeedController feedController = new FeedController();

    public ProfileController(String username) throws FileNotFoundException {
        this.username = username;
    }

    private PreparedStatement makePstmt() {
        String query = "SELECT id, post_image, post_date, username FROM posts WHERE username = ?";

        PreparedStatement pstmt;
        try {
            pstmt = Jdbc.getConnection().prepareStatement(query);
            pstmt.setString(1, username);
            return pstmt;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    private void takeMeHome() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) homeButton.getScene().getWindow();
        stage.setScene(scene);
        String feed_css = this.getClass().getResource("Feed2.css").toExternalForm();
        scene.getStylesheets().add(feed_css);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image profileImage = feedController.getProfilePic(feedController.getUserId(this.username));
        realProfileImage.setFill(new ImagePattern(profileImage));
        usernameLabel.setText(username);
        usernameLabel.setAlignment(Pos.CENTER);
        loadUserInfo();
        String sql = "SELECT id, post_image, likes, post_date,username FROM posts WHERE username = " + username + ")";
        postsListView.setPrefWidth(570);
        postsListView.setTranslateX(-15);
        feedController.displayPosts(feedController.loadPosts(makePstmt()), postsListView);
    }

    // Method to load user profile from the database
    public List getAdditionalUserInfo(int id) {
        List<String> userInfo = new ArrayList();
        String sql = "SELECT first_name, last_name FROM users WHERE id = ?";
        try (Connection conn = Jdbc.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                userInfo.add(firstName);
                userInfo.add(lastName);
                return userInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Return false in case of an error or if the query fails
        }
        return null;
    }

    public List getUserInfo(int id) {
        List userInfo = new ArrayList();
        String sql = "SELECT height, weight, bio, goal_height, goal_weight FROM user_details WHERE id = ?";
        try (Connection conn = Jdbc.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bio = rs.getString("bio");
                userWeight = rs.getInt("weight");
                userHeight = rs.getInt("height");
                goal_weight = rs.getInt("goal_weight");
                goal_height = rs.getInt("goal_height");
                userInfo.add(bio);
                userInfo.add(userWeight);
                userInfo.add(userHeight);
                userInfo.add(goal_weight);
                userInfo.add(goal_height);
                return userInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Return false in case of an error or if the query fails
        }
        return null;
    }

//        profileImageView.setImage(image);


    private void loadUserInfo() {
        Label name = new Label();
        String fullName = getAdditionalUserInfo(feedController.getUserId(username)).get(0) + " " + getAdditionalUserInfo(feedController.getUserId(username)).get(1);
        name.setText(fullName);
        Label bioField = new Label();
        bioField.setMaxHeight(100);
        Label weight = new Label();
        Label height = new Label();
        List userInfo = getUserInfo(feedController.getUserId(username));
        bioField.setText("\n" + (String) userInfo.get(0));
        if (userWeight != 0) {
            weight.setText("\nWeight: " + userInfo.get(1) + " KG");
        }
        if (userHeight != 0) {
            height.setText("Height: " + userInfo.get(2) + " CM");
        }

        vBox1.getChildren().addAll(name, bioField, weight, height);

    }

    public void goToUserSettings() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ProfileEditor.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) settings.getScene().getWindow();
        stage.setScene(scene);
        String feed_css = this.getClass().getResource("ProfileSettingsStyling.css").toExternalForm();
        scene.getStylesheets().add(feed_css);
        stage.show();
    }

}