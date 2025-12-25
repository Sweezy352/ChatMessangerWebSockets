package com.example.webchatapplication.services;

import com.example.webchatapplication.dto.TokenAuthenticationDto;
import com.example.webchatapplication.dto.request.AuthenticationRequest;
import com.example.webchatapplication.dto.response.UserDtoResponse;
import com.example.webchatapplication.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
    public UserEntity register(UserEntity userEntity);
    public TokenAuthenticationDto login(AuthenticationRequest authenticationRequest);
    public void loginWithEmail(String email);
    public TokenAuthenticationDto verifyCodeFromEmail(String email, String code);
    public UserEntity getCurrentAuthenticated();
}
