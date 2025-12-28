package com.example.webchatapplication.exception;

public class AccessDeniedException extends BaseException {
    public AccessDeniedException(String message) {
        super(ExceptionResponse.builder().status(403).error(message).build());
    }
}
