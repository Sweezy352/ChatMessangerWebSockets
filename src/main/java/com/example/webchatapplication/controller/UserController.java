package com.example.webchatapplication.controller;

import com.example.webchatapplication.dto.response.UserDtoResponse;
import com.example.webchatapplication.dto.view.UserDtoView;
import com.example.webchatapplication.mapper.UserMapper;
import com.example.webchatapplication.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<UserDtoResponse> getById(@PathVariable("id") Long id){
        return ResponseEntity.ok(userMapper.toDtoResponse(userService.getById(id)));
    }

    @GetMapping("/get-by-username")
    public ResponseEntity<List<UserDtoView>> getByUsername(@RequestParam("username") String username){
        return ResponseEntity.ok(userMapper.toDtoViewList(userService.getByUsername(username)));
    }

    @PostMapping("/set-bio")
    public ResponseEntity<UserDtoResponse> setBio(@RequestParam("bio") String bio){
        return ResponseEntity.ok(userMapper.toDtoResponse(userService.setBio(bio)));
    }
}
