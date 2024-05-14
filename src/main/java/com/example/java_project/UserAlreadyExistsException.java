package com.example.java_project;

public class UserAlreadyExistsException extends Exception{
    UserAlreadyExistsException(String message){
        super(message);
    }
}
