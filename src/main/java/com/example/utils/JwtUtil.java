package com.example.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secretKey}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken() {
        return Jwts.builder().setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1小时有效期
                .signWith(secretKey).compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
            // 检查 token 是否过期
            Date expiration = claims.getExpiration();
            return expiration.after(new Date());

        } catch (ExpiredJwtException e) {
            // Token 已过期
            return false;
        } catch (Exception e) {
            // 其他验证错误
            return false;
        }
    }
}

   