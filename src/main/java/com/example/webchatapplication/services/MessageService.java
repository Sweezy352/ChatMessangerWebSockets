package com.example.webchatapplication.services;

import com.example.webchatapplication.entity.MessageEntity;
import com.example.webchatapplication.entity.UserEntity;

import java.util.List;

public interface MessageService {
    MessageEntity saveMessage(Long recipientId, MessageEntity message, UserEntity sender);
    List<MessageEntity> getAllMessages(Long chatId);
    void deleteMessage(Long messageId);
    MessageEntity getMessageById(Long messageId);
    List<MessageEntity> getByContextMessages(Long chatId,String context);
    MessageEntity updateMessage(Long messageId,MessageEntity message);
}
