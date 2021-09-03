package com.nti.socialmediaappcore.controller;

import com.nti.socialmediaappcore.model.UserIdentity;
import com.nti.socialmediaappcore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/identities")
    public List<UserIdentity> getUserIdentitiesByFullNameContains(@RequestParam String fullName) {
        return userService.getUserIdentitiesByFullNameContains(fullName);
    }
}
