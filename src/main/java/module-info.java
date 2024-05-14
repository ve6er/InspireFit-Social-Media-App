module com.example.java_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;
    requires com.google.gson;
    requires spring.web;


    opens com.example.java_project to javafx.fxml;
    exports com.example.java_project;
}