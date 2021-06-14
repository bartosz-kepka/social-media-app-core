package com.nti.socialmediaappcore.controller;

import com.nti.socialmediaappcore.dto.NewReactionDTO;
import com.nti.socialmediaappcore.dto.NewPostDTO;
import com.nti.socialmediaappcore.model.Post;
import com.nti.socialmediaappcore.repository.PostRepository;
import com.nti.socialmediaappcore.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    PostRepository postRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>>getAllPostsPage(@RequestParam(required = false) String title,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "3") int size){
            try {
                List<Post> posts = new ArrayList<Post>();
                Pageable paging = PageRequest.of(page, size);

                Page<Post> pagePosts;
                if (title == null)
                    pagePosts = postRepository.findAll(paging);
                else
                    pagePosts = postRepository.findByIdContainingIgnoreCase(title, paging);

                posts = pagePosts.getContent();

                Map<String, Object> response = new HashMap<>();
                response.put("posts", posts);
                response.put("currentPage", pagePosts.getNumber());
                response.put("totalItems", pagePosts.getTotalElements());
                response.put("totalPages", pagePosts.getTotalPages());

                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

    }

//    @GetMapping
//    public List<Post> getPosts() {
//        return postService.getAllPosts();
//    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Post addPost(@RequestBody @Valid NewPostDTO newPostDTO) {
        return postService.addPost(newPostDTO);
    }

    @PatchMapping("/{postid}/reactions")
    public Post makeReaction(@PathVariable String postid, @RequestBody NewReactionDTO reactionDTO) {
        return postService.makeReaction(postid, reactionDTO);
    }


}
