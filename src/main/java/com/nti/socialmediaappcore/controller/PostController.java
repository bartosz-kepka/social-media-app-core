package com.nti.socialmediaappcore.controller;

import com.nti.socialmediaappcore.dto.NewPostDTO;
import com.nti.socialmediaappcore.model.Post;
import com.nti.socialmediaappcore.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @GetMapping
    public List<Post> getPosts() {
        return postService.getAllPosts();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Post addPost(@RequestBody @Valid NewPostDTO newPostDTO) {
        return postService.addPost(newPostDTO);
    }
}

