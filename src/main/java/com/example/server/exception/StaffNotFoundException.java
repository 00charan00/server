package com.example.server.exception;

public class StaffNotFoundException extends RuntimeException{
    public StaffNotFoundException(String msg){
        super(msg);
    }
}
