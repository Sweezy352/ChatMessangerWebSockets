package com.example.webchatapplication.services.impl;

import com.example.webchatapplication.dto.TokenAuthenticationDto;
import com.example.webchatapplication.dto.request.AuthenticationRequest;
import com.example.webchatapplication.entity.UserEntity;
import com.example.webchatapplication.exception.AuthenticationFailedException;
import com.example.webchatapplication.exception.UserNotFoundException;
import com.example.webchatapplication.repository.UserRepository;
import com.example.webchatapplication.security.JwtCore;
import com.example.webchatapplication.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtCore jwtCore;

    @Override
    public UserEntity register(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    @Override
    public TokenAuthenticationDto login(AuthenticationRequest authenticationRequest) {

        UserEntity userEntity = userRepository.findByUsernameOrPhoneNumber(authenticationRequest.getIdentifier(), authenticationRequest.getIdentifier()).orElseThrow(() -> new UserNotFoundException("User not found"));
        if(!passwordEncoder.matches(authenticationRequest.getPassword(), userEntity.getPassword())) throw new AuthenticationFailedException("Incorrect username or password");
        return new TokenAuthenticationDto(jwtCore.generateJwtToken(userEntity));
    }

    @Override
    public void loginWithEmail(String email) {
    }

    @Override
    public TokenAuthenticationDto verifyCodeFromEmail(String email, String code) {
        return null;
    }

    @Override
    public UserEntity getCurrentAuthenticated() {
        return (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
