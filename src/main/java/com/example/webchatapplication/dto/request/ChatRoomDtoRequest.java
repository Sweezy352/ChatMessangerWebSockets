package com.example.webchatapplication.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDtoRequest {
    private String chatRoomType;
    @NotNull(message = "Cannot create chat room without users")
    private List<Long> userIds;
}
