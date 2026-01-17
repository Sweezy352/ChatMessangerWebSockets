package com.example.webchatapplication.exception;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String message) {
        super(ExceptionResponse.builder()
                .status(404)
                .error(message).build());
    }
}
