package com.example.webchatapplication.exception;

public class AttachmentNotFoundException extends BaseException {
    public AttachmentNotFoundException(String message) {
        super(ExceptionResponse.builder().status(404).error(message).build());
    }
}
