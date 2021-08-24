package com.nti.socialmediaappcore.jwt;

import com.nti.socialmediaappcore.exception.AuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final String EXCEPTION_TEXT = "Unauthorized error";

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws AuthException {
        throw new AuthException(MessageFormat.format(
                EXCEPTION_TEXT, null
        ));
    }

}

