package com.example.webchatapplication.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private final ExceptionResponse exceptionResponse;

    public BaseException(ExceptionResponse exceptionResponse) {
        super(exceptionResponse.getError());
        this.exceptionResponse = exceptionResponse;
    }
}
