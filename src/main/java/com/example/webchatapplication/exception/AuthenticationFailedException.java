package com.example.webchatapplication.exception;

public class AuthenticationFailedException extends BaseException {
    public AuthenticationFailedException(String message) {
        super(ExceptionResponse.builder().status(401).error(message).build());
    }
}
