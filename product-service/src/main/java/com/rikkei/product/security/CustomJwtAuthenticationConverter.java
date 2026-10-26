package com.rikkei.product.security;

import com.rikkei.product.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomJwtAuthenticationConverter {
    private final JwtProperties jwtProperties;

    public CustomJwtAuthenticationConverter(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public AbstractAuthenticationToken convert(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();

            // 1. Lay List<String> tu claim "permissions"
            List<?> rawPermissions = claims.get("permissions", List.class);

            // 2. Map tung String thanh SimpleGrantedAuthority
            List<SimpleGrantedAuthority> authorities = (rawPermissions == null)
                    ? List.of()
                    : rawPermissions.stream()
                            .map(Object::toString)
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            // 3. Tra ve UsernamePasswordAuthenticationToken chua danh sach Authorities nay
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}
