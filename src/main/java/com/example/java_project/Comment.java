package com.example.java_project;
import javafx.scene.image.Image;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Comment {
    private int id;
    private int postId;
    private String text;
    private LocalDateTime date;
    private String userName;
    private Boolean isPositive;

    // Constructor
    public Comment(int postId, String text, LocalDateTime date, String userName, Boolean isPositive) {
        this.postId = postId;
        this.text = text;
        this.date = date;
        this.userName= userName;
        this.isPositive= isPositive;
    }
    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", postId=" + postId +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", username=" + userName +
                '}';
    }


    // Getters and Setters


    public Boolean getIsPositive() {
        return isPositive;
    }

    public void setIsPositive(Boolean positive) {
        isPositive = positive;
    }

    public String getUserName(int post_id) {
        return userName;
    }


    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}

