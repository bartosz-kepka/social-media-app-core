package com.nti.socialmediaappcore.exception;

import java.io.IOException;

public class AuthException extends IOException {
    public AuthException(String message) {
        super(message);
    }
}
