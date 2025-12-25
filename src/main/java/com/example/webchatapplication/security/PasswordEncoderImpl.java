package com.example.webchatapplication.security;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class PasswordEncoderImpl implements PasswordEncoder {
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public PasswordEncoderImpl() {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder(8);
    }

    @Override
    public @Nullable String encode(@Nullable CharSequence rawPassword) {
        return bCryptPasswordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(@Nullable CharSequence rawPassword, @Nullable String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}
