package com.nti.socialmediaappcore.repository;

import com.nti.socialmediaappcore.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepository extends MongoRepository<Post, String> {
    Page<Post> findAllByOrderByDateDesc(Pageable pageable);
}
