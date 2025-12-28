package com.example.webchatapplication.services.impl;

import com.example.webchatapplication.entity.ChatRoomEntity;
import com.example.webchatapplication.entity.MessageEntity;
import com.example.webchatapplication.entity.UserEntity;
import com.example.webchatapplication.exception.AccessDeniedException;
import com.example.webchatapplication.exception.MessageNotFound;
import com.example.webchatapplication.repository.MessageRepository;
import com.example.webchatapplication.services.AuthService;
import com.example.webchatapplication.services.ChatRoomService;
import com.example.webchatapplication.services.MessageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final AuthService authService;
    private final ChatRoomService chatRoomService;
    private final MessageRepository messageRepository;

    @Override
    @Transactional
    public MessageEntity saveMessage(Long recipientId, MessageEntity message, UserEntity sender) {
        System.out.println("C message Service -------->>>>>>>>>>>>>>:" + message.getContent());
        ChatRoomEntity chatRoomEntity = chatRoomService.getOrCreatePrivateChatRoom(recipientId, sender);
        message.setChatRoomEntity(chatRoomEntity);
        message.setSender(sender);
        return messageRepository.save(message);
    }

    @Override
    public List<MessageEntity> getAllMessages(Long chatId) {
        return messageRepository.findAllByChatRoomEntityId(chatId);
    }

    @Override
    @Transactional
    public void deleteMessage(Long messageId) {
        UserEntity currentUser = authService.getCurrentAuthenticated();
        MessageEntity messageEntity = getMessageById(messageId);
        if(!messageEntity.getSender().getId().equals(currentUser.getId())) throw new AccessDeniedException("You are not allowed to delete this message");
        messageRepository.deleteById(messageId);
    }

    @Override
    public MessageEntity getMessageById(Long messageId) {
        return messageRepository.findById(messageId).orElseThrow(() -> new MessageNotFound("Message not found"));
    }

    @Override
    public List<MessageEntity> getByContextMessages(Long chatId, String context) {
        return messageRepository.findByChatRoomEntityIdAndContentContaining(chatId, context);
    }

    @Override
    @Transactional
    public MessageEntity updateMessage(Long messageId,MessageEntity message) {
        UserEntity currentUser = authService.getCurrentAuthenticated();
        MessageEntity messageEntity = getMessageById(messageId);
        if(!messageEntity.getSender().getId().equals(currentUser.getId())) throw new AccessDeniedException("You are not allowed to update this message");
        messageEntity.setContent(message.getContent());
        return messageRepository.save(messageEntity);
    }
}
