package com.example.webchatapplication.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDtoRequest {
    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 4, max = 15, message = "Username must be between 4 and 15 characters")
    private String username;
    @NotNull(message = "Phone number cannot be null")
    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNumber;
    @NotNull(message = "Mail cannot be null")
    @NotBlank(message = "Mail cannot be blank")
    private String mail;
    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    private String password;
    @NotNull(message = "Age cannot be null")
    private Integer age;
    @NotNull(message = "Birth date cannot be null")
    private LocalDate birthDate;
}
