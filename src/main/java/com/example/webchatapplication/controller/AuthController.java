package com.example.webchatapplication.controller;

import com.example.webchatapplication.dto.TokenAuthenticationDto;
import com.example.webchatapplication.dto.request.AuthenticationRequest;
import com.example.webchatapplication.dto.request.UserDtoRequest;
import com.example.webchatapplication.dto.response.UserDtoResponse;
import com.example.webchatapplication.exception.BaseException;
import com.example.webchatapplication.mapper.UserMapper;
import com.example.webchatapplication.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<UserDtoResponse> register(@RequestBody UserDtoRequest userDtoRequest) throws BaseException {
        return ResponseEntity.ok(userMapper.toDtoResponse(authService.register(userMapper.toEntity(userDtoRequest))));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenAuthenticationDto> login(@RequestBody AuthenticationRequest authenticationRequest) throws BaseException{
        System.out.println("----------->>>>>>>>>>>>>" + authenticationRequest.getIdentifier() + " " + authenticationRequest.getPassword());
        return ResponseEntity.ok(authService.login(authenticationRequest));
    }

    @PostMapping("/login/email")
    public ResponseEntity<String> loginWithEmail(@RequestParam String email) throws BaseException{
        return ResponseEntity.ok("");
    }

    @PostMapping("/verify/email")
    public ResponseEntity<TokenAuthenticationDto> verifyCodeFromEmail(@RequestParam String email, @RequestParam String code) throws BaseException{
        return ResponseEntity.ok(authService.verifyCodeFromEmail(email, code));
    }

}
