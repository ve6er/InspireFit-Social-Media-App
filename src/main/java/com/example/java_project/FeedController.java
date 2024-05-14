package com.example.java_project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.scene.control.ListCell;


import static com.example.java_project.Jdbc.getConnection;

public class FeedController {
    @FXML
    private ImageView imageView;
    @FXML
    private Label greetingLabel; // Label to display greeting message
    @FXML
    private Button uploadButton;
    @FXML
    private Button profileButton;
    @FXML
    private ListView<Post> postsListView;
    @FXML
    private Button loadButton;
    @FXML
    private ImageView topIcon;
    @FXML
    private ImageView profileImageView;


    InputStream stream1 = new FileInputStream("src\\main\\resources\\com\\example\\java_project\\Like.png");
    Image like = new Image(stream1);
    InputStream stream2 = new FileInputStream("src\\main\\resources\\com\\example\\java_project\\Unlike.png");
    Image unlike= new Image(stream2);

    String sql = "SELECT id, post_image, likes, post_date,username FROM posts WHERE post_date >= DATE_SUB(NOW(), INTERVAL 45 DAY)";



    public FeedController() throws FileNotFoundException {
    }

    @FXML
    private void initialize() {
        Image profileImage = getProfilePic(getUserId(Controller.loggedInUsername));
        profileImageView.setImage(profileImage);
        profileImageView.setTranslateX(10);
        postsListView.setTranslateX(-24.5);
        postsListView.setTranslateY(10);
        postsListView.setPrefWidth(570);
        // Display greeting message with the logged-in username
        greetingLabel.setText("Hello, " + Controller.loggedInUsername + "!");
        displayPosts(loadPosts(makePstmt()), postsListView);
    }

    private PreparedStatement makePstmt(){
        String query = "SELECT id, post_image, post_date, username FROM posts WHERE post_date >= ?";

        PreparedStatement pstmt;

        try {

            pstmt = Jdbc.getConnection().prepareStatement(query);

            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().minusDays(45)));

            List<Post> posts = loadPosts(pstmt);
            return pstmt;

        } catch (SQLException e) {

            e.printStackTrace();

        }
        return null;
    }

    public void loadPosts(){
        displayPosts(loadPosts(makePstmt()), postsListView);
    }
    private List<Comment> retrieveCommentsForPost(int postId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT id, comment, comment_date, username, is_positive FROM comments WHERE post_id = ?";

        try (Connection conn = Jdbc.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int commentId = rs.getInt("id");
                String text = rs.getString("comment");
                LocalDateTime commentDate = rs.getTimestamp("comment_date").toLocalDateTime();
                String username = rs.getString("username");
                Boolean isPositive = rs.getBoolean("is_positive");
                if (isPositive) {
                    Comment comment = new Comment(postId, text, commentDate, username, isPositive);

                    comments.add(comment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comments;
    }


    public List<Post> loadPosts(PreparedStatement pstmt) {
        List<Post> allPosts = new ArrayList<>();

        try (Connection conn = Jdbc.getConnection();
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Extract information from the database
                int postId = rs.getInt("id");
                byte[] imageData = rs.getBytes("post_image");
                String username = rs.getString("username");
                LocalDateTime postDate = rs.getTimestamp("post_date").toLocalDateTime();

                // Convert binary image data to JavaFX Image object
                Image image = convertToImage(imageData);

                // Create a Post object
                Post post = new Post(postId, image, postDate, username);
                // Retrieve comments for this post
                List<Comment> comments = retrieveCommentsForPost(postId);
                post.setComments(comments);

                // Add the post to the list
                allPosts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Display the posts in the ListView
//        displayPosts(allPosts);

        return allPosts;
    }


    public void displayPosts(List<Post> allPosts, ListView<Post> postsListView) {
        postsListView.getItems().clear();

        // Set custom cell factory to display posts and comments
        postsListView.setCellFactory(new Callback<ListView<Post>, ListCell<Post>>() {
            @Override
            public ListCell<Post> call(ListView<Post> param) {
                return new ListCell<Post>() {
                    @Override
                    protected void updateItem(Post item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            VBox postContainer = new VBox();
                            HBox posterInfo = new HBox();
                            Image profilePic = getProfilePic(getUserId(item.getUsername()));
                            ImageView imageView0 = new ImageView();
                            imageView0.setImage(getProfilePic(getUserId(item.getUsername())));
//                            Image post_image_test = getSpecificPost(1);
//                            imageView0.setImage(post_image_test);
                            imageView0.setFitHeight(25);

                            imageView0.setFitWidth(25);
                            imageView0.setTranslateY(8);

                            Label posterUsername = new Label();
                            posterUsername.setText(item.getUsername());
                            posterUsername.setTranslateY(10);
                            posterUsername.setTranslateX(4);
                            posterInfo.getChildren().addAll(imageView0, posterUsername);
                            // Create an HBox to hold the post image and details
                            HBox container = new HBox();
//                            container.setSpacing(10);
                            postContainer.getChildren().addAll(posterInfo,container);
                            postContainer.setSpacing(10);
                            // Display post image
                            ImageView imageView1 = new ImageView(item.getImage());
                            imageView1.setFitWidth(200); // Adjust width as needed
                            imageView1.setFitHeight(200); // Adjust height as needed

                            // Create a VBox to hold the post details
                            VBox postDetails = new VBox();
                            postDetails.setSpacing(5);
                            postDetails.setTranslateX(8);
                            // Display post likes
                            Label likesLabel = new Label("Likes: " + item.getLikes());

                            // Display post date
                            Label dateLabel = new Label("Date: " + item.getPostDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                            ImageView imageView2 = new ImageView(like);
                            imageView2.setFitWidth(25); // Adjust width as needed
                            imageView2.setFitHeight(25); // Adjust height as needed
                            imageView2.setOnMouseClicked(event -> likePost(item.getId(), Controller.loggedInUsername, imageView2, postsListView));
                            if (!(hasLikedPost(item.getId(), Controller.loggedInUsername))){
                                imageView2.setImage(unlike);
                            } else{
                                imageView2.setImage(like);
                            }
                            // Add post details to the VBox
                            postDetails.getChildren().addAll(likesLabel, dateLabel, imageView2);

                            VBox commentSection = new VBox();

                            // Display comments in a ListView
                            ListView<String> commentsListView = new ListView<>();
                            commentsListView.getItems().addAll(item.getCommentText());
                            commentsListView.setMaxHeight(100);

                            commentsListView.setMaxWidth(150);

                            // Add post image and details, and comments to the container
                            TextField commentField = new TextField();
                            commentField.setPromptText("Add Comment");
                            commentField.setOnAction(event -> addComment(item, commentField.getText(), Controller.loggedInUsername));
                            commentField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                                if (event.getCode() == KeyCode.ENTER) {
                                    addComment(item, commentField.getText(), Controller.loggedInUsername);
                                    // Consume the event to prevent further processing
                                    event.consume();
                                }
                            });
                            commentField.setMaxWidth(150);
                            commentSection.getChildren().addAll(commentsListView,commentField);
                            commentSection.setSpacing(10);
                            commentSection.setTranslateX(8);
                            // Add post image and details, comments, and comment field to the container
                            container.getChildren().addAll(imageView1, postDetails, commentSection);
                            // Set the container as the graphic for the cell
                            setGraphic(postContainer);
                        }
                    }
                };
            }
        });

        // Add all the posts to the ListView
        postsListView.getItems().addAll(allPosts);
    }

    public void likePost(int postId, String username, ImageView imageView2, ListView<Post> postsListView){
        String like_sql = "INSERT INTO likes (post_id, username) VALUES (?, ?)";
        String unlike_sql = "DELETE FROM likes WHERE username = ? AND post_id = ?";
        if (!(hasLikedPost(postId, username))){
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(like_sql)) {

                // Set the username
                stmt.setInt(1, postId);

                // Set the image as a blob
                stmt.setString(2, username);

                // Execute the query
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            imageView2.setImage(like);

        } else{

            try (Connection conn = getConnection();

                 PreparedStatement stmt = conn.prepareStatement(unlike_sql)) {

                // Set the image as a blob
                stmt.setString(1, username);
                stmt.setInt(2, postId);

                // Execute the query
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            imageView2.setImage(unlike);
        } displayPosts(loadPosts(makePstmt()), postsListView);
    }

    public Image getSpecificPost(int id){
        String sql_profile= "SELECT post_image FROM posts WHERE id = ?";
        try (Connection conn = Jdbc.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql_profile)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                byte[] imageData = rs.getBytes("post_image");
                Image image =convertToImage(imageData);

                return image;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Return false in case of an error or if the query fails
        }
        return null;
    }

    public Image getProfilePic(int id){
        String sql = "SELECT profile_photo FROM user_details WHERE id = ?";
        try (Connection conn = Jdbc.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                byte[] imageData = rs.getBytes("profile_photo");
                Image image = convertToImage(imageData);
                return image;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Return false in case of an error or if the query fails
        }
        return null;
    }

    public Integer getUserId(String username){
        String sql = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = Jdbc.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Return false in case of an error or if the query fails
        }
        System.out.println("Error in getUserId");
        return null;

    }

    public boolean hasLikedPost(int postId, String username) {
        String sql = "SELECT * FROM likes WHERE post_id = ? AND username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            stmt.setString(2, username);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // If rs.next() returns true, it means the user has liked the post
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of an error or if the query fails
        }
    }


    public void addComment(Post post, String commentText, String userName) {
        // Create a new Comment object with the given text and current date

        // Add the comment to the post
        Boolean filtered = post.moderation(commentText);
        if (!filtered){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Comment Blocked");
            alert.setHeaderText(null);
            alert.setContentText("The comment you have posted may violate the guidelines set by the post owner");
            alert.showAndWait();
        }
        Comment comment = new Comment(post.getId(), commentText, LocalDateTime.now(), Controller.loggedInUsername, filtered);
        post.addComment(comment);
        displayPosts(loadPosts(makePstmt()), postsListView);
    }

    // This method converts your byte array to a JavaFX Image object
    private Image convertToImage(byte[] imageData) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData)) {
            return new Image(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    private void logout() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        String login_css = this.getClass().getResource("Login_Styling.css").toExternalForm();
        scene.getStylesheets().add(login_css);
        Stage stage = (Stage) loadButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void goToProfile(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("profile.fxml"));
        ProfileController controller = new ProfileController(Controller.loggedInUsername);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) topIcon.getScene().getWindow();
        String profile_css = this.getClass().getResource("Profile_Styling.css").toExternalForm();
        scene.getStylesheets().add(profile_css);
        stage.setScene(scene);
        stage.show();
    }

    private void goToForeignProfile(){

    }


    @FXML
    private void uploadPhoto() {
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
                insertImageToDatabase(byteArrayInputStream);

                displayPosts(loadPosts(makePstmt()), postsListView);
                // Close the input stream
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void insertImageToDatabase(InputStream inputStream) {
        String sql = "INSERT INTO posts (username, post_date, post_image) VALUES (?, NOW(), ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the username
            stmt.setString(1, Controller.loggedInUsername);

            // Set the image as a blob
            stmt.setBinaryStream(2, inputStream);

            // Execute the query
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}