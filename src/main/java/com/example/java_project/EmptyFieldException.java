package com.example.java_project;

import java.security.spec.ECFieldF2m;

public class EmptyFieldException extends Exception {
    EmptyFieldException(String message){
        super(message);
    }
}
