package com.nti.socialmediaappcore.dto;

import com.nti.socialmediaappcore.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private String email;

    private String firstName;

    private String lastName;

    private String description;

    public UserDTO(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.description = user.getDescription();
        this.email = user.getEmail();
    }
}
