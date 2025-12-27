package com.example.webchatapplication.services;

import com.example.webchatapplication.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserEntity getById(Long id);
    List<UserEntity> getByUsername(String username);
    UserEntity setBio(String bio);
}
