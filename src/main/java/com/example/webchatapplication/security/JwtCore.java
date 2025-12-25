package com.example.webchatapplication.security;

import com.example.webchatapplication.entity.UserEntity;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtCore {
    @Value("${jwt_secretKey}")
    private String jwtSecret;
    @Value("${jwt_token_expiration}")
    private Long tokenExpiration;
    private SecretKey secretKey;

    public SecretKey getSecretKey(){
        if(secretKey == null){
            byte[] jwtSecretBytes = Decoders.BASE64.decode(jwtSecret);
            secretKey = Keys.hmacShaKeyFor(jwtSecretBytes);
        }
        return secretKey;
    }

    public String generateJwtToken(UserDetails userDetails){
        UserEntity userEntity = (UserEntity) userDetails;
        Date tokenCreated = new Date();
        Date tokenExpiration = new Date(tokenCreated.getTime() + this.tokenExpiration);

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userEntity.getId());
        claims.put("username", userEntity.getUsername());

        return Jwts.builder().claims().add(claims)
                .subject(userEntity.getUsername())
                .issuedAt(tokenCreated)
                .expiration(tokenExpiration)
                .and()
                .signWith(getSecretKey())
                .compact();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException ex){
            log.error("JWT token is expired: {}", ex.getMessage());
        }catch (UnsupportedJwtException ex){
            log.error("JWT token is unsupported: {}", ex.getMessage());
        }catch (MalformedJwtException ex){
            log.error("JWT token is invalid: {}", ex.getMessage());
        }catch (SignatureException ex){
            log.error("JWT signature is invalid: {}", ex.getMessage());
        }catch (SecurityException ex){
            log.error("JWT signature is invalid: {}", ex.getMessage());
        }catch (IllegalArgumentException ex){
            log.error("JWT claims string is empty: {}", ex.getMessage());
        }catch (Exception ex){
            log.error("JWT token is invalid: {}", ex.getMessage());
        }

        return false;
    }

    public String extractUsername(String token){
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }
}
