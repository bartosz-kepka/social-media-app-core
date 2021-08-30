package com.nti.socialmediaappcore.service;

import com.nti.socialmediaappcore.dto.LoginDTO;
import com.nti.socialmediaappcore.exception.InvalidCredentialsException;
import com.nti.socialmediaappcore.exception.RoleNotFoundException;
import com.nti.socialmediaappcore.exception.UserAlreadyRegistered;
import com.nti.socialmediaappcore.jwt.JwtUtils;
import com.nti.socialmediaappcore.model.ERole;
import com.nti.socialmediaappcore.model.Role;
import com.nti.socialmediaappcore.model.User;
import com.nti.socialmediaappcore.dto.CredentialsDTO;
import com.nti.socialmediaappcore.dto.RegisterDTO;
import com.nti.socialmediaappcore.repository.RoleRepository;
import com.nti.socialmediaappcore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AuthService {
    private static final String EMAIL_EXISTS = "There is already a user registered with email " +
            "\"{0}\"";
    private static final String USER_ROLE_NOT_FOUND = "User role not found";
    private static final String INVALID_CREDENTIALS_MESSAGE = "Provided email or password is " +
            "incorrect";

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    public LoginDTO authenticate(@Valid @RequestBody CredentialsDTO credentialsDTO) {
        User user = userRepository.findByEmailIgnoreCase(credentialsDTO.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException(INVALID_CREDENTIALS_MESSAGE));

        if (!isAuthenticate(credentialsDTO.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException(INVALID_CREDENTIALS_MESSAGE);
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, credentialsDTO, null);
        String token = jwtUtils.generateJwtToken(authentication);

        return new LoginDTO(token, user);
    }

    public void register(@Valid @RequestBody RegisterDTO registerDTO) {
        if (userRepository.existsByEmailIgnoreCase(registerDTO.getEmail())) {
            throw new UserAlreadyRegistered(MessageFormat.format(
                    EMAIL_EXISTS, registerDTO.getEmail()));
        }

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException(USER_ROLE_NOT_FOUND));
        roles.add(userRole);

        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setPassword(encoder.encode(registerDTO.getPassword()));
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setRoles(roles);

        userRepository.save(user);
    }

    private boolean isAuthenticate(String providedPassword, String userPassword) {
        return encoder.matches(providedPassword, userPassword);
    }

    public void init() {
        if (roleRepository.findAll().isEmpty()) {
            roleRepository.insert(new Role(ERole.ROLE_USER));
            roleRepository.insert(new Role(ERole.ROLE_ADMIN));
        }
    }
}
