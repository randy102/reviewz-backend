package com.backend.security;

import com.backend.user.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtil {
    private String JWT_SECRET = "secret";

    public String sign(UserEntity user, String ip){
        Map<String, Object> claims = new HashMap<>();

        claims.put("name", user.getUsername());
        claims.put("roles", user.getRoles());
        claims.put("id", user.getId());
        claims.put("img", user.getImg());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET+ip)
                .compact();
    }

    public Claims decode(String token, String ip){
        return Jwts.parser().setSigningKey(JWT_SECRET+ip).parseClaimsJws(token).getBody();
    }

    public Boolean verify(String token, String ip){
        try{
            Jwts.parser().setSigningKey(JWT_SECRET+ip).parseClaimsJws(token);
            return true;
        }
        catch (JwtException err){
            return false;
        }

    }
}
