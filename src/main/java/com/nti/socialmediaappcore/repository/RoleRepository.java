package com.nti.socialmediaappcore.repository;

import com.nti.socialmediaappcore.model.ERole;
import com.nti.socialmediaappcore.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}

