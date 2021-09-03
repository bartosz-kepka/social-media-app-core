package com.nti.socialmediaappcore.jwt;


import com.nti.socialmediaappcore.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    @Value("${nti.app.jwtSecret}")
    private String jwtSecret;

    @Value("${nti.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(user.getId())
                .setIssuer(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.decode(jwtSecret))
                .compact();
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getIssuer();
    }

    public String getIdFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | IllegalArgumentException | UnsupportedJwtException | MalformedJwtException | ExpiredJwtException e) {
            return false;
        }
    }
}
