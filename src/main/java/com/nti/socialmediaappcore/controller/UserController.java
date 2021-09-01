package com.nti.socialmediaappcore.controller;

import com.nti.socialmediaappcore.dto.EditUserDTO;
import com.nti.socialmediaappcore.dto.UserDTO;
import com.nti.socialmediaappcore.model.User;
import com.nti.socialmediaappcore.model.UserIdentity;
import com.nti.socialmediaappcore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{userId}")
    public User editUser(@PathVariable String userId, @RequestBody EditUserDTO editUserDTO) {
        return userService.editUser(editUserDTO, userId);
    }

    @GetMapping("/{userId}")
    public UserDTO getUser(@PathVariable String userId) {
        return userService.getUser(userId);
    }
}
