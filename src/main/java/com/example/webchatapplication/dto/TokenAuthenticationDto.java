package com.example.webchatapplication.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenAuthenticationDto {
    private String token;
}
