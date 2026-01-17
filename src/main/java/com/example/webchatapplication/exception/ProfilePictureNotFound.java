package com.example.webchatapplication.exception;

public class ProfilePictureNotFound extends BaseException {
    public ProfilePictureNotFound(String message) {
        super(ExceptionResponse.builder().status(404).error(message).build());
    }
}
