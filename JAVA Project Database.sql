CREATE DATABASE fitness_app;

USE fitness_app;


CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name CHAR(255),
    last_name CHAR(255),
    age INT,
    INDEX (username)  -- Add an index on the username column
);

CREATE TABLE posts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    post_date DATETIME,
    post_image MEDIUMBLOB
    FOREIGN KEY (username) REFERENCES users(username)
);

CREATE TABLE comments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT,
    username VARCHAR(255),
    comment TEXT,
    comment_date DATETIME,
    likes INT,
    is_positive BIT DEFAULT FALSE,
    FOREIGN KEY (post_id) REFERENCES posts(id),
    FOREIGN KEY (username) REFERENCES users(username)
);

CREATE TABLE likes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT,
    username VARCHAR(255),
    FOREIGN KEY (post_id) REFERENCES posts(id),
    FOREIGN KEY (username) REFERENCES users(username)
);

CREATE TABLE user_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    height DOUBLE,
    weight DOUBLE,
    goal_height DOUBLE,
    goal_weight DOUBLE,
    bio TEXT,
    profile_photo MEDIUMBLOB,
    FOREIGN KEY (id) REFERENCES users(id)
);
