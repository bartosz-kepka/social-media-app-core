package com.nti.socialmediaappcore.model;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nti.socialmediaappcore.dto.RegisterDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;

    @Email
    private String email;

    @JsonIgnore
    private String password;

    private String firstName;

    private String lastName;

    @DBRef
    private Set<Role> roles = new HashSet<>();

    public User(RegisterDTO registerDTO,
                String encodedPassword,
                Set<Role> roles) {
        this.email = registerDTO.getEmail();
        this.password = encodedPassword;
        this.firstName = registerDTO.getFirstName();
        this.lastName = registerDTO.getLastName();
        this.roles = roles;
    }
}
