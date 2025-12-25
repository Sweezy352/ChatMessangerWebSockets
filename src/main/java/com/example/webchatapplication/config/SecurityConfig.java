package com.example.webchatapplication.config;

import com.example.webchatapplication.security.JwtAuthenticationFilter;
import com.example.webchatapplication.security.JwtCore;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter authenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http.cors(cors -> cors.disable());
        http.csrf(csrf -> csrf.disable());

        http.httpBasic(Customizer.withDefaults());
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/api/auth/register").permitAll()
                            .requestMatchers("/api/auth/login").permitAll()
                            .requestMatchers("/api/auth/login/email").permitAll()
                            .requestMatchers("/api/auth/verify/email").permitAll();
            auth.anyRequest().authenticated();
        }).sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
