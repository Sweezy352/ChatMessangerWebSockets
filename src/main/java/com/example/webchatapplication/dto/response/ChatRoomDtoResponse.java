package com.example.webchatapplication.dto.response;

import com.example.webchatapplication.dto.view.UserDtoView;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDtoResponse {
    private Long id;
    private String chatRoomType;
    private List<UserDtoView> userDtoViews;
    private List<MessageDtoResponse> messageDtoResponses;

}
