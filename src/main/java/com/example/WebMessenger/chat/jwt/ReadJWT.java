package com.example.WebMessenger.chat.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.Base64;

public class ReadJWT {
    final static private String secretKey = Base64.getEncoder().encodeToString("TheKoon".getBytes());

    public static String getUser(String token) {
        // 토큰 검증
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            String user = claims.getSubject();
            Object roles = claims.get("roles");

            return user;
        }

        // 문자열 오류
        catch (IllegalArgumentException e) {
            return null;
        }

    }

}
