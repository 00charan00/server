package com.example.server.controller;

import com.example.server.exception.DataNotFoundException;
import com.example.server.exception.InvalidDataException;
import com.example.server.exception.StaffNotFoundException;
import com.example.server.responsemodel.ResponseBase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@CrossOrigin
public class StaffExceptionHandler {

    @ExceptionHandler(StaffNotFoundException.class)
    public ResponseEntity<ResponseBase> handleNotFound(StaffNotFoundException exception){
        ResponseBase responseBase = new ResponseBase(exception.getMessage(),false);
        return new ResponseEntity<>(responseBase, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InvalidDataException.class, DataNotFoundException.class})
    public ResponseEntity<ResponseBase> handleInvalid(Exception exception){
        ResponseBase responseBase = new ResponseBase(exception.getMessage(),false);
        return new ResponseEntity<>(responseBase, HttpStatus.BAD_REQUEST);
    }
}