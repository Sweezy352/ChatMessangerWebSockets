package com.example.webchatapplication.controller;

import com.example.webchatapplication.dto.request.MessageDtoRequest;
import com.example.webchatapplication.dto.response.ChatRoomDtoResponse;
import com.example.webchatapplication.dto.response.MessageDtoResponse;
import com.example.webchatapplication.entity.MessageEntity;
import com.example.webchatapplication.entity.UserEntity;
import com.example.webchatapplication.exception.BaseException;
import com.example.webchatapplication.mapper.ChatRoomMapper;
import com.example.webchatapplication.mapper.MessageMapper;
import com.example.webchatapplication.services.AuthService;
import com.example.webchatapplication.services.ChatRoomService;
import com.example.webchatapplication.services.MessageService;
import com.example.webchatapplication.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatRoomService chatRoomService;
    private final AuthService authService;
    private final MessageService messageService;
    private final ChatRoomMapper chatRoomMapper;
    private final MessageMapper messageMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/get-or-create-private-chat")
    public ResponseEntity<ChatRoomDtoResponse> getOrCreatePrivateChat(@RequestParam("recipientId") Long recipientId) throws BaseException{
        return ResponseEntity.ok(chatRoomMapper.toDtoResponse(chatRoomService.getOrCreatePrivateChatRoom(recipientId, authService.getCurrentAuthenticated())));
    }

    @GetMapping("/get-my-chats")
    public ResponseEntity<List<ChatRoomDtoResponse>> getMyChats() throws BaseException{
        return ResponseEntity.ok(chatRoomMapper.toDtoResponseList(chatRoomService.getMyChats()));
    }

    @PostMapping("/messages")
    public ResponseEntity<MessageDtoResponse> sendMessage(@RequestParam Long recipientId, @RequestBody MessageDtoRequest messageDtoRequest) throws BaseException{
        return ResponseEntity.ok(messageMapper.toDtoResponse(messageService.saveMessage(recipientId, messageMapper.toEntity(messageDtoRequest), authService.getCurrentAuthenticated())));
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<MessageDtoResponse>> getChatHistory(@PathVariable(name = "chatId") Long chatId) throws BaseException{
        return ResponseEntity.ok(messageMapper.toDtoResponseList(messageService.getAllMessages(chatId)));
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload MessageDtoRequest messageDtoRequest, Principal principal) throws BaseException {
        UserEntity sender = (UserEntity) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        System.out.println("----------->>>>>>>>>>>>>>>>>>>>:" + messageDtoRequest.getContent());
        MessageEntity savedMessage = messageService.saveMessage(messageDtoRequest.getRecipientId(), messageMapper.toEntity(messageDtoRequest), sender);
        UserEntity recipientEntity = savedMessage.getChatRoomEntity().getUserEntities().stream().filter(userEntity -> !userEntity.getId().equals(sender.getId())).findFirst().get();
        messagingTemplate.convertAndSendToUser(
                recipientEntity.getUsername(),
                "/queue/messages",
                messageMapper.toDtoResponse(savedMessage)
        );

    }
}
