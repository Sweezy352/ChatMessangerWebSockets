package com.example.webchatapplication.mapper;

import com.example.webchatapplication.dto.request.MessageDtoRequest;
import com.example.webchatapplication.dto.response.MessageDtoResponse;
import com.example.webchatapplication.entity.MessageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface MessageMapper {
    @Mapping(target = "content", source = "content")
    MessageEntity toEntity(MessageDtoRequest messageDtoRequest);
    @Mapping(target = "sender", source = "sender")
    @Mapping(target = "chatId", source = "chatRoomEntity.id")
    MessageDtoResponse toDtoResponse(MessageEntity messageEntity);
    List<MessageDtoResponse> toDtoResponseList(List<MessageEntity> messageEntities);
}
