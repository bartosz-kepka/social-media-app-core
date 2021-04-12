package com.nti.socialmediaappcore.repository;

import com.nti.socialmediaappcore.model.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Set;

public interface ChatRepository extends MongoRepository<Chat, String> {
    boolean existsByMembersIds(Set<String> membersIds);
}
