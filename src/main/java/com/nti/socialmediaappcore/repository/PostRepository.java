package com.nti.socialmediaappcore.repository;

import com.nti.socialmediaappcore.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
}
