package com.example.java_project;


import java.io.InputStream;
import java.sql.*;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

public class Jdbc {

    private static final String URL = "jdbc:mysql://localhost:3306/fitness_app";
    private static final String USER = "root"; // Update with your MySQL username
    private static final String PASSWORD = "danny3Ric"; // Update with your MySQL password

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void addUser(String username, String password, String first_name, String last_name, int age) {
        String sql = "INSERT INTO users (username, password, first_name, last_name, age) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password); // Consider hashing the password before storing
            stmt.setString(3, first_name);
            stmt.setString(4, last_name);
            stmt.setInt(5, age);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addAdditionalDetails(double height, double weight, String bio, InputStream profile_photo) {
        String sql = "INSERT INTO user_details (height, weight, bio, profile_photo) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

//        stmt.setInt(1, id);
            stmt.setDouble(1, height);
            stmt.setDouble(2, weight);
            stmt.setString(3, bio);
            stmt.setBinaryStream(4, profile_photo);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}