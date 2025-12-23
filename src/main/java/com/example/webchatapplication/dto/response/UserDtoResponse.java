package com.example.webchatapplication.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDtoResponse {
    private Long id;
    private String username;
    private String phoneNumber;
    private String mail;
    private String bio;
    private Integer age;
    private LocalDate birthDate;
    private LocalDate dateRegistered;
    private List<Long> pfpPicturesId;
}
