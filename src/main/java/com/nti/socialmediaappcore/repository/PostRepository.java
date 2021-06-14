package com.nti.socialmediaappcore.repository;

import com.nti.socialmediaappcore.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepository extends MongoRepository<Post, String> {
//    Page<Post> findByAuthor(String authorId, Pageable pageable);
//
    Page<Post> findByIdContainingIgnoreCase(String id, Pageable pageable);
}
