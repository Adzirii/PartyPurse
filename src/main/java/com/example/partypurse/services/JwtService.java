package com.example.partypurse.services;

import com.example.partypurse.dto.UserClaims;
import com.example.partypurse.dto.JwtType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {
    private static final String SECRET_KEY = "dGhpcyBzZWNyZXQga2V5IGlzIGVuY29kZWQgdGV4dCBieSBiYXNlNjQgYWxnb3JpdGht";
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(SECRET_KEY);
        secretKey = Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(UserClaims userClaims, JwtType jwtType) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .claim("username", userClaims.username())
                .claim("firstName", userClaims.firstName())
                .claim("lastName", userClaims.lastName())
                .claim("id", userClaims.id())
                .subject(userClaims.username())
                .issuedAt(new Date(now))
                .expiration(new Date(now + jwtType.getExpireTime()))
                .signWith(secretKey)
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = exctractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String jwt){
        return extractClaim(jwt, Claims::getSubject);
    }

    public Date extractExpiration(String jwt){
        return extractClaim(jwt, Claims::getExpiration);
    }

    public Claims exctractAllClaims(final String jwt) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt).getPayload();
        } catch (UnsupportedJwtException ex) {
            log.info("[JwtService] UnsupportedJwtException");
        } catch (JwtException ex) {
            log.info("[JwtService] JwtException");
        } catch (IllegalArgumentException ex) {
            log.info("[JwtService] IllegalArgumentException");
        } catch (Exception ex) {
            log.info("[JwtService] Exception");
        }
        throw new IllegalArgumentException("Invalid Token");
    }

}
