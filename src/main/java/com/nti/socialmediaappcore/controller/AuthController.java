package com.nti.socialmediaappcore.controller;

import javax.validation.Valid;

import com.nti.socialmediaappcore.dto.CredentialsDTO;
import com.nti.socialmediaappcore.dto.RegistrationDTO;
import com.nti.socialmediaappcore.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody CredentialsDTO credentialsDTO) {
        return authService.authenticateUser(credentialsDTO);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationDTO signUpRequest) {
        return authService.registerUser(signUpRequest);
    }
}