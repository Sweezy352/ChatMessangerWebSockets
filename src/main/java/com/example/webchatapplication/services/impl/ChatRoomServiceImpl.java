package com.example.webchatapplication.services.impl;

import com.example.webchatapplication.entity.ChatRoomEntity;
import com.example.webchatapplication.entity.UserEntity;
import com.example.webchatapplication.enums.ChatRoomType;
import com.example.webchatapplication.exception.UserNotFoundException;
import com.example.webchatapplication.repository.ChatRoomRepository;
import com.example.webchatapplication.repository.UserRepository;
import com.example.webchatapplication.services.AuthService;
import com.example.webchatapplication.services.ChatRoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final AuthService authService;
    private final UserRepository userRepository;

    @Override
    public ChatRoomEntity getOrCreatePrivateChatRoom(Long recipientId, UserEntity sender) {
        return chatRoomRepository.findPrivateChatBetweenUsers(sender.getId(), recipientId).orElseGet(() -> {
             return createPrivateChatRoom(recipientId);
         });
    }

    @Override
    public List<ChatRoomEntity> getMyChats() {
        return chatRoomRepository.findAllByUserId(authService.getCurrentAuthenticated().getId());
    }

    private ChatRoomEntity createPrivateChatRoom(Long recipientId){
        UserEntity sender = authService.getCurrentAuthenticated();
        UserEntity recipient = userRepository.findById(recipientId).orElseThrow(() -> new UserNotFoundException("User not found"));
        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder().chatRoomType(ChatRoomType.PRIVATE).userEntities(List.of(sender, recipient)).build();
        return chatRoomRepository.save(chatRoomEntity);
    }
}
