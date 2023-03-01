package com.caffe.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtUtil {

    private String ACCESS_TOKEN_SECRET = "ddthilindra";

    // Get data from token
    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Decode token with secret key
    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(ACCESS_TOKEN_SECRET).parseClaimsJws(token).getBody();
    }

    // Validate the token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals((userDetails.getUsername())) && !isTokenExpired(token));
    }

    // Get username from token
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    // Get expiration from token
    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    // Check the token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Generate token
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username);
    }

    // Create token
    public String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)).signWith(SignatureAlgorithm.HS256, ACCESS_TOKEN_SECRET).compact();
    }

}
