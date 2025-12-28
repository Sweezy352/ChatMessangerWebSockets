package com.example.webchatapplication.dto.response;

import com.example.webchatapplication.dto.view.UserDtoView;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDtoResponse {
    private Long id;
    private String content;
    private UserDtoView sender;
    private LocalDateTime dateSent;
    private String status;
    private Long chatId;
}
