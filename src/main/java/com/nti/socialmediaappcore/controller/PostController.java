package com.nti.socialmediaappcore.controller;

import com.nti.socialmediaappcore.dto.NewReactionDTO;
import com.nti.socialmediaappcore.dto.NewPostDTO;
import com.nti.socialmediaappcore.dto.PageDTO;
import com.nti.socialmediaappcore.model.Post;
import com.nti.socialmediaappcore.service.PostService;
import com.nti.socialmediaappcore.util.identity.WithIdentities;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @GetMapping
    public WithIdentities<PageDTO<Post>> getAllPostsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return this.postService.getAllPostsPage(page, size);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Post addPost(@RequestBody @Valid NewPostDTO newPostDTO) {
        return postService.addPost(newPostDTO);
    }

    @PatchMapping("/{postId}/reactions")
    public void makeReaction(@PathVariable String postId, @RequestBody NewReactionDTO reactionDTO) {
        postService.makeReaction(postId, reactionDTO);
    }
}
