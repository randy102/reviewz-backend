package com.backend.security;

import com.backend.user.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtil {
    private String JWT_SECRET = "secret";

    public String sign(UserEntity user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getUsername());
        claims.put("roles", user.getRoles());
        claims.put("id", user.getId());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
    }

    public Claims decode(String token){
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
    }

    public Boolean verify(String token){
        try{
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        }
        catch (JwtException err){
            return false;
        }

    }
}
