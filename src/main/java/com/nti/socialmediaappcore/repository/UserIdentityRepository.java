package com.nti.socialmediaappcore.repository;

import com.nti.socialmediaappcore.model.UserIdentity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface UserIdentityRepository extends MongoRepository<UserIdentity, String> {
    List<UserIdentity> findAllByFullNameContainingIgnoreCase(String fullName);
    Set<UserIdentity> findAllBy_idIn(Set<String> ids);
}
