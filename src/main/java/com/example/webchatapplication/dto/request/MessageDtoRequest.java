package com.example.webchatapplication.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDtoRequest {
    @NotNull(message = "Cannot send message without context")
    @NotEmpty(message = "Cannot send message without context")
    private String content;
    private Long recipientId;
}
