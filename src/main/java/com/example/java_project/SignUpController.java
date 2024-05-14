package com.example.java_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.io.IOException;
import java.util.Objects;

import static com.example.java_project.Jdbc.getConnection;

public class SignUpController {
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField userName;
    @FXML
    private Button nextButton;
    @FXML
    private DatePicker datePicker;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label error;

    int age;
    String first_name;
    String last_name;
    String user_name;
    String password;
    String confirm_password;



    public int getAge() {
        LocalDate date = datePicker.getValue();
        LocalDate currentDate = LocalDate.now();
        if (date != null) {
            return Period.between(date, currentDate).getYears();
        } else {
            return 0;
        }

    }

    static void checkAge(int age) throws AgeException{
        if (age<16){
            throw new AgeException("You must be at least 16 years old to register.");
        }
    }

    private Boolean userExists(String username){
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // If rs.next() returns true, it means the user already exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of an error or if the query fails
        }
    }

    public void goNext(ActionEvent event) throws IOException, AgeException, EmptyFieldException, UserAlreadyExistsException {
        first_name = firstName.getText().trim();
        last_name = lastName.getText().trim();
        user_name = userName.getText().trim();
        password = passwordField.getText().trim();
        confirm_password = confirmPasswordField.getText().trim();
        LocalDate date = datePicker.getValue();
        age = getAge();
        if (age<16){
            error.setVisible(true);
            error.setText("You must be at least 16 years old to register.");
            error.setTextFill(Paint.valueOf("red"));
            error.setTextAlignment(TextAlignment.CENTER);
        }
        checkAge(age);
        if (first_name.isEmpty() || last_name.isEmpty() || user_name.isEmpty() || password.isEmpty() || confirm_password.isEmpty()){
            error.setText("All fields are compulsory");
            error.setTextFill(Paint.valueOf("red"));
            error.setTextAlignment(TextAlignment.CENTER);
            error.setVisible(true);
            throw new EmptyFieldException("All fields are compulsory");
        }
        if (userExists(user_name)){
            error.setText("The user already exists");
            error.setTextFill(Paint.valueOf("red"));
            error.setTextAlignment(TextAlignment.CENTER);
            error.setVisible(true);
            throw new UserAlreadyExistsException("The username already exists");

        }

        if (Objects.equals(password, confirm_password) && date != null) {
            Jdbc jdbc = new Jdbc();
            System.out.println(user_name);
            jdbc.addUser(user_name, hashPassword(password), first_name, last_name, age);
            System.out.println("Record Added");
            Parent root = FXMLLoader.load(getClass().getResource("SignUp2.fxml"));
            Scene scene = new Scene(root);
            String login_css = this.getClass().getResource("SignUpStylings.css").toExternalForm();
            scene.getStylesheets().add(login_css);
            Stage stage = (Stage) nextButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else {
            if (date == null) {
                System.out.println("Please enter a valid date");
            } else {
                System.out.println("Your passwords are not matching");
            }

        }

    }

    public String hashPassword(String password) {
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // Add password bytes to digest
            md.update(password.getBytes());
            // Get the hashed bytes
            byte[] hashedBytes = md.digest();
            // Convert bytes to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Handle exception (e.g., log it, throw a custom exception, etc.)
            return null;
        }
    }
}
