package com.example.webchatapplication.exception;

public class MessageNotFound extends BaseException {
    public MessageNotFound(String message) {
        super(ExceptionResponse.builder().status(404).error(message).build());
    }
}
