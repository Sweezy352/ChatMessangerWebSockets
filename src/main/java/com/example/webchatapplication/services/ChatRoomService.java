package com.example.webchatapplication.services;

import com.example.webchatapplication.entity.ChatRoomEntity;
import com.example.webchatapplication.entity.UserEntity;

import java.util.List;

public interface ChatRoomService {
    ChatRoomEntity getOrCreatePrivateChatRoom(Long recipientId, UserEntity sender);
    List<ChatRoomEntity> getMyChats();
}
