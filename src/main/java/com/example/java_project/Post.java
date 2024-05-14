package com.example.java_project;

import javafx.scene.image.Image;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.java_project.Jdbc.getConnection;
import com.google.gson.Gson;
import org.springframework.web.client.RestTemplate;

class Post {
    private Image image;
//    private int likes;
    private LocalDateTime postDate;
    private int id;
    private List<Comment> comments;
    private String username;


    public Post(int id, Image image, LocalDateTime postDate, String username) {
        this.id= id;
        this.image = image;
        this.postDate = postDate;
        this.username= username;
    }

    public int getId(){
        return this.id;
    }
    public Image getImage() {
        return image;
    }


    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    public void addComment(Comment newComment){
        comments.add(newComment);
        try (Connection connection = getConnection()) {
            // Prepare SQL statement for inserting comments
            String sql = "INSERT INTO comments (post_id, comment, comment_date, username, is_positive) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, newComment.getPostId());
            statement.setString(2, newComment.getText());
            statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(4, Controller.loggedInUsername);
            statement.setBoolean(5, newComment.getIsPositive());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private final String apibase = "http://127.0.0.1:8000";

    public Boolean moderation(String comment) {
        RestTemplate restTemplate = new RestTemplate();
        String commentUrl = String.format("%s/filter-comment/?comment=%s", apibase, comment);
        String jsonResponse = restTemplate.getForObject(commentUrl, String.class);
        Gson gson = new Gson();
        return gson.fromJson(jsonResponse, Boolean.class);
    }

    public List<Comment> getComments(){
        return this.comments;

    }

    public List<String> getCommentText(){
        List<String> commentText = new ArrayList<>();
        for (Comment comment: comments){
            commentText.add(comment.getUserName(this.id)+": "+ comment.getText());
        }
        return commentText;
    }

    public String getUsername() {
        return username;
    }

    public int getLikes() {
        int likes=0;
        String sql = "SELECT COUNT(*) FROM likes WHERE post_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                likes = rs.getInt(1); // Get the count of likes from the first column
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return likes;
    }

    public LocalDateTime getPostDate() {
        return postDate;
    }
}
