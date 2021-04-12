package com.nti.socialmediaappcore.service;

import com.nti.socialmediaappcore.dto.NewPostDTO;
import com.nti.socialmediaappcore.model.Post;
import com.nti.socialmediaappcore.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    private final ModelMapper modelMapper;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post addPost(NewPostDTO newPostDTO) {
        Post post = modelMapper.map(newPostDTO, Post.class);

        return postRepository.insert(post);
    }
}
