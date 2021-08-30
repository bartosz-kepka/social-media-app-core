package com.nti.socialmediaappcore.controller;

import javax.validation.Valid;

import com.nti.socialmediaappcore.dto.CredentialsDTO;
import com.nti.socialmediaappcore.dto.LoginDTO;
import com.nti.socialmediaappcore.dto.RegisterDTO;
import com.nti.socialmediaappcore.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginDTO authenticate(@Valid @RequestBody CredentialsDTO credentialsDTO) {
        return authService.authenticate(credentialsDTO);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterDTO registerDTO) {
        authService.register(registerDTO);
    }

    @PostMapping("/init")
    public void init() {
        authService.init();
    }
}
