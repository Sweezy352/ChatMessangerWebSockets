package com.example.webchatapplication.exception;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionResponse {
    private int status;
    private String error;
    private Date timestamp;
}
