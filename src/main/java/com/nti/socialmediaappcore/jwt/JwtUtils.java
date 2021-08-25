package com.nti.socialmediaappcore.jwt;


import com.nti.socialmediaappcore.exception.AuthException;
import com.nti.socialmediaappcore.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${nti.app.jwtSecret}")
    private String jwtSecret;

    @Value("${nti.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.decode(jwtSecret))
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) throws AuthException {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | IllegalArgumentException | UnsupportedJwtException | MalformedJwtException | ExpiredJwtException e) {
            throw new AuthException(MessageFormat.format(
                    e.getMessage(),
                    e
            ));
        }
    }
}
