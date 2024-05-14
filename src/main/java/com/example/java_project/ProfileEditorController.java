package com.example.java_project;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import java.io.*;
import java.util.List;
import javafx.stage.FileChooser;


public class ProfileEditorController extends ProfileController implements Initializable {

    @FXML
    Circle profileImageCircle;
    @FXML
    TextField firstNameField;
    @FXML
    TextField lastNameField;
    @FXML
    TextArea bioText;
    @FXML
    ImageView up1;
    @FXML
    ImageView up2;
    @FXML
    ImageView up3;
    @FXML
    ImageView up4;
    @FXML
    ImageView down1;
    @FXML
    ImageView down2;
    @FXML
    ImageView down3;
    @FXML
    ImageView down4;
    @FXML
    Label weightLabel;
    @FXML
    Label goalWeightLabel;
    @FXML
    Label heightLabel;
    @FXML
    Label goalHeightLabel;
    @FXML
    Button replacePhoto;
    @FXML
    Button removePhoto;
    @FXML
    ImageView back;


    private String firstName;
    private String lastName;
    private String bio;
    private int weight;
    private int goalWeight;
    private int height;
    private int goalHeight;
    private List userInfo;
    private List additonalUserInfo;
    private int id;
    private ByteArrayInputStream byteArrayInputStream;

    public ProfileEditorController() throws FileNotFoundException {
        super(Controller.loggedInUsername);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        username = Controller.loggedInUsername;
        id = feedController.getUserId(username);
        userInfo= (List) getUserInfo(feedController.getUserId(username));
        additonalUserInfo= getAdditionalUserInfo(feedController.getUserId(username));
        firstNameField.setText((String) additonalUserInfo.get(0));
        lastNameField.setText((String) additonalUserInfo.get(1));
        setDetails();
        bioText.setText(bio);
        weightLabel.setText(weight+" KG");
        weightLabel.setAlignment(Pos.BASELINE_CENTER);
        heightLabel.setText(height+" CM");
        heightLabel.setAlignment(Pos.BASELINE_CENTER);
        goalWeightLabel.setText(goalWeight+ " KG");
        goalWeightLabel.setAlignment(Pos.BASELINE_CENTER);
        goalHeightLabel.setText(goalHeight+" CM");
        goalHeightLabel.setAlignment(Pos.BASELINE_CENTER);
        Image profileImage = feedController.getProfilePic(feedController.getUserId(this.username));
        profileImageCircle.setFill(new ImagePattern(profileImage));
    }

    private void setDetails(){
        bio = (String) userInfo.get(0);
        weight = (Integer) userInfo.get(1);
        height= (Integer) userInfo.get(2);
        goalWeight = (Integer) userInfo.get(3);
        goalHeight = (Integer) userInfo.get(4);
    }



    public void saveChanges() {
        firstName= firstNameField.getText();
        lastName= lastNameField.getText();
        if (byteArrayInputStream==null) {
            String sql = "UPDATE users SET first_name = ?, last_name = ? WHERE id = ?";
            try (Connection conn = Jdbc.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, firstName);
                stmt.setString(2, lastName);
                stmt.setInt(3, id);
                int rowsUpdated = stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else{
            String sql = "UPDATE user_details SET profile_photo = ? WHERE id = ?";
            try (Connection conn = Jdbc.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setBinaryStream(1, byteArrayInputStream);
                stmt.setInt(2,id);
                int rowsUpdated = stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("Database updated");
        } updateAdditionalDetails();

    }

    private void updateAdditionalDetails(){
        String sql = "UPDATE user_details SET weight = ?, height = ?, goal_weight = ?, goal_height = ? WHERE id = ?";
        try (Connection conn = Jdbc.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, weight);
            stmt.setInt(2, height);
            stmt.setInt(3, goalWeight);
            stmt.setInt(4, goalHeight);
            stmt.setInt(5, id);
            int rowsUpdated = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void incrementWeight(){
        weight+=1;
        weightLabel.setText(weight+" KG");
    }
    @FXML
    private void decrementWeight(){
        weight-=1;
        weightLabel.setText(weight+" KG");
    }
    @FXML
    private void incrementGoalWeight(){
        goalWeight+=1;
        goalWeightLabel.setText(goalWeight+ " KG");
    }
    @FXML
    private void decrementGoalWeight(){
        goalWeight-=1;
        goalWeightLabel.setText(goalWeight+ " KG");
    }

    @FXML
    private void incrementHeight(){
        height+=1;
        heightLabel.setText(height+" CM");
    }
    @FXML
    private void decrementHeight(){
        height-=1;
        heightLabel.setText(height+" CM");
    }
    @FXML
    private void incrementGoalHeight(){
        goalHeight+=1;
        goalHeightLabel.setText(goalHeight+ " CM");
    }
    @FXML
    private void decrementGoalHeight(){
        goalHeight-=1;
        goalHeightLabel.setText(goalHeight+ " CM");
    }

    public ByteArrayInputStream replaceProfile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Stage stage = (Stage) replacePhoto.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                // Read the selected image file
                FileInputStream inputStream = new FileInputStream(selectedFile);
                FileInputStream inputStream2 = new FileInputStream(selectedFile);
                Image image = new Image(inputStream2);
                profileImageCircle.setFill(new ImagePattern(image));
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
                this.byteArrayInputStream= byteArrayInputStream;
                return byteArrayInputStream;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } return null;
    }

    @FXML
    private void backToProfile() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("profile.fxml"));
        ProfileController controller = new ProfileController(Controller.loggedInUsername);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) replacePhoto.getScene().getWindow();
        String profile_css = this.getClass().getResource("Profile_Styling.css").toExternalForm();
        scene.getStylesheets().add(profile_css);
        stage.setScene(scene);
        stage.show();
    }
}






