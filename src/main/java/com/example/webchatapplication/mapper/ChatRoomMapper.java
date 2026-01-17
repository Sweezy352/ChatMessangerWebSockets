package com.example.webchatapplication.mapper;

import com.example.webchatapplication.dto.request.ChatRoomDtoRequest;
import com.example.webchatapplication.dto.response.ChatRoomDtoResponse;
import com.example.webchatapplication.dto.view.ChatRoomDtoView;
import com.example.webchatapplication.entity.ChatRoomEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, MessageMapper.class})
public interface ChatRoomMapper {


    ChatRoomEntity toEntity(ChatRoomDtoRequest chatRoomRequest);

    @Mapping(target = "userDtoViews", source = "userEntities")
    @Mapping(target = "messageDtoResponses", source = "messageEntities")
    ChatRoomDtoResponse toDtoResponse(ChatRoomEntity chatRoomEntity);
    List<ChatRoomDtoResponse> toDtoResponseList(List<ChatRoomEntity> chatRoomEntities);

    @Mapping(target = "userDtoViews", source = "userEntities")
    ChatRoomDtoView toDtoView(ChatRoomEntity chatRoomEntity);

    List<ChatRoomDtoView> toDtoViewList(List<ChatRoomEntity> chatRoomEntities);

}
