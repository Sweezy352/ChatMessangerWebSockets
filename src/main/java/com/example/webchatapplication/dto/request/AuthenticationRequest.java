package com.example.webchatapplication.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRequest {
    private String identifier;
    private String password;
}
