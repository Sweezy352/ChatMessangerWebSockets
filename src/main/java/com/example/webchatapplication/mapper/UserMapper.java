package com.example.webchatapplication.mapper;

import com.example.webchatapplication.dto.request.UserDtoRequest;
import com.example.webchatapplication.dto.response.UserDtoResponse;
import com.example.webchatapplication.dto.view.UserDtoView;
import com.example.webchatapplication.entity.PfpPictureEntity;
import com.example.webchatapplication.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(UserDtoRequest userDtoRequest);

    @Mapping(target = "pfpPicturesId", source = "pfpPictureEntities", qualifiedByName = "mapPfpPicturesToIds")
    UserDtoResponse toDtoResponse(UserEntity userEntity);

    List<UserDtoResponse> toDtoResponseList(List<UserEntity> userEntities);

    @Mapping(target = "pfpProfileId", source = "pfpPictureEntities", qualifiedByName = "mapPfpPictureToId")
    UserDtoView toDtoView(UserEntity userEntity);

    List<UserDtoView> toDtoViewList(List<UserEntity> userEntities);

    @Named("mapPfpPicturesToIds")
    default List<Long> mapPfpPicturesToIds(List<PfpPictureEntity> pfpPictureEntities){
        if(pfpPictureEntities == null || pfpPictureEntities.isEmpty()){
            return null;
        }
        return pfpPictureEntities.stream().map(PfpPictureEntity::getId).toList();
    }

    @Named("mapPfpPictureToId")
    default Long mapPfpPictureToId(List<PfpPictureEntity> pfpPictureEntities){
        if(pfpPictureEntities == null || pfpPictureEntities.isEmpty()){
            return null;
        }
        return pfpPictureEntities.get(0).getId();
    }
}
