
package com.example.java_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.java_project.Jdbc.getConnection;

public class Controller {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;
    @FXML
    private Label error;

    // Static variable to store the logged-in username
    public static String loggedInUsername;

    static boolean authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?"; // Consider using hashed passwords

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // In a real application, you'd hash this password before checking

            ResultSet rs = stmt.executeQuery();

            // If the result set has data, the user exists
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        SignUpController signUp = new SignUpController();
        // Check credentials

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (authenticateUser(username,signUp.hashPassword(password))) {
            // Store the logged-in username
            loggedInUsername = username;

            // Load the new scene
            Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            String feed_css = this.getClass().getResource("Feed2.css").toExternalForm();
            scene.getStylesheets().add(feed_css);
            stage.show();
        } else {
            // Display an error message or handle invalid credentials
            System.out.println("Invalid username or password");
            error.setText("Invalid username or password");
        }
    }

    public void signUp(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        Scene scene = new Scene(root);
        String login_css = this.getClass().getResource("SignUpStylings.css").toExternalForm();
        scene.getStylesheets().add(login_css);
        Stage stage = (Stage) signupButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}

