package com.example.webchatapplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> catchUsernameNotFoundException(BaseException ex){
        return ResponseEntity.status(HttpStatusCode.valueOf(ex.getExceptionResponse().getStatus()))
                .body(ExceptionResponse.builder()
                        .status(HttpStatus.valueOf(ex.getExceptionResponse().getStatus()).value())
                        .error(ex.getMessage())
                        .timestamp(new Date())
                        .build());
    }
}
