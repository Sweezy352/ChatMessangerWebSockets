package com.example.webchatapplication.mapper;

import com.example.webchatapplication.dto.request.MessageDtoRequest;
import com.example.webchatapplication.dto.response.MessageDtoResponse;
import com.example.webchatapplication.entity.MessageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface MessageMapper {
    MessageEntity toEntity(MessageDtoRequest messageDtoRequest);
    @Mapping(target = "sender", source = "sender")
    MessageDtoResponse toDtoResponse(MessageEntity messageEntity);
    List<MessageDtoResponse> toDtoResponseList(List<MessageEntity> messageEntities);
}
