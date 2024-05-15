show databases;
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
drop table posts;
CREATE TABLE posts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    post_date DATETIME,
    post_image MEDIUMBLOB,
    likes INT DEFAULT 0,
    FOREIGN KEY (username) REFERENCES users(username)
);
drop table comments;
CREATE TABLE comments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT,
    username VARCHAR(255),
    comment TEXT,
    comment_date DATETIME,
    likes INT,
    FOREIGN KEY (post_id) REFERENCES posts(id),
    FOREIGN KEY (username) REFERENCES users(username)
);
drop table likes;
CREATE TABLE likes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT,
    username VARCHAR(255),
    FOREIGN KEY (post_id) REFERENCES posts(id),
    FOREIGN KEY (username) REFERENCES users(username)
);

drop table user_details;
drop table users;

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
drop table posts;
drop table users;
drop table user_details;
drop table comments;
drop table likes;





ALTER TABLE comments
ADD COLUMN is_positive BIT DEFAULT FALSE;

ALTER TABLE posts
DROP COLUMN likes;

select * from users;	  
select * from user_details;
select * from posts;
select * from comments;
select * from likes;
SELECT LENGTH(post_image) AS photo_size FROM posts WHERE id = 7;
SELECT LENGTH(profile_photo) AS photo_size FROM user_details;
SELECT u.username, ud.bio, ud.profile_photo FROM users u JOIN user_details ud ON u.id = ud.id WHERE u.username = "verr";
DELETE from posts WHERE id=1; 
