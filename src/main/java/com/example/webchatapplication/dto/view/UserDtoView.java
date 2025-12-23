package com.example.webchatapplication.dto.view;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDtoView {
    private Long id;
    private String username;
    private Long pfpProfileId;
}
