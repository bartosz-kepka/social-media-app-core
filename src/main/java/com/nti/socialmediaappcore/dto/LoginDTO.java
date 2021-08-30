package com.nti.socialmediaappcore.dto;

import com.nti.socialmediaappcore.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    private String token;
    private User user;
}
