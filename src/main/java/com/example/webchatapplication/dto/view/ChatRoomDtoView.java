package com.example.webchatapplication.dto.view;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDtoView {
    private Long id;
    private String chatRoomType;
    private List<UserDtoView> userDtoViews;
}
