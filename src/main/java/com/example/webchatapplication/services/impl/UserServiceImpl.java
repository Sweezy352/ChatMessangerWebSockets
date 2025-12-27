package com.example.webchatapplication.services.impl;

import com.example.webchatapplication.entity.UserEntity;
import com.example.webchatapplication.exception.UserNotFoundException;
import com.example.webchatapplication.repository.UserRepository;
import com.example.webchatapplication.services.AttachmentService;
import com.example.webchatapplication.services.AuthService;
import com.example.webchatapplication.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthService authService;

    @Override
    public UserEntity getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public List<UserEntity> getByUsername(String username) {
        return userRepository.findAllByUsernameIsLike(username);
    }

    @Override
    public UserEntity setBio(String bio) {
        UserEntity userEntity = authService.getCurrentAuthenticated();
        if(userEntity.getBio().equals(bio)) return userEntity;
        userEntity.setBio(bio);
        return userRepository.save(userEntity);
    }
}
