package com.springbot.tttn.application.utils;

import com.springbot.tttn.application.exceptions.MissingTokenException;
import com.springbot.tttn.application.security.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expired}")
    private int jwtExpiration;

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        //create jwt
        return Jwts.builder()
                .setSubject(userDetails.getUsername() + userDetails.getAuthorities()) // grant jwt for user
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key(), SignatureAlgorithm.HS256) // sign
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwt(String token) {

        if (token.contains("Bearer")) token = token.substring(7);

        String decodeJwt = Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();

        String[] stringToArrayDecodeJwt = decodeJwt.split("\\[");

        return stringToArrayDecodeJwt[0];
    }

    public void validateJwtToken(String authToken) throws MissingTokenException, MalformedJwtException, ExpiredJwtException {
        if (authToken == null) {
            throw new MissingTokenException("Missing token");
        }
        Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
    }
}
