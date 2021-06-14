package com.nti.socialmediaappcore.service;

import com.nti.socialmediaappcore.dto.NewPostDTO;
import com.nti.socialmediaappcore.dto.NewReactionDTO;
import com.nti.socialmediaappcore.exception.NotFoundException;
import com.nti.socialmediaappcore.model.*;
import com.nti.socialmediaappcore.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    private static final String NOT_FOUND_MESSAGE = "Post with id \"{0}\" doesn't exist";

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post addPost(NewPostDTO newPostDTO) {
        Post post = modelMapper.map(newPostDTO, Post.class);
        post.setReactions(new HashMap<>());

        return postRepository.insert(post);
    }

    public Post getPost(String postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                        new NotFoundException(MessageFormat.format(NOT_FOUND_MESSAGE, postId))
                                                          );
    }

    public Post makeReaction(String postId, NewReactionDTO reactionDTO) {
        Post post = getPost(postId);
        Reactions.reaction reactionType = reactionDTO.getType();
        String senderId = reactionDTO.getSenderId();
        Reactions.reaction none = Reactions.reaction.none;

        HashMap<String, Reactions.reaction> reactions = post.getReactions();

        if (!reactions.containsKey(senderId) && reactionType != none) {
            //post nie ma reakcji, przekazany typ reakcji inny niz none
            reactions.put(senderId, reactionType);
        } else if (reactions.get(senderId) != none && reactions.get(senderId) != reactionType && reactionType != none) {
            //jest like dajemy dislike i odwrotnie
            reactions.replace(senderId, reactionType);
        } else if (reactions.containsKey(senderId) && !reactions.get(senderId).equals(none)) {
            //post ma reakcje, cofniecie reakcji
            reactions.remove(senderId, reactionType);
        } else throw new NotFoundException(MessageFormat.format(NOT_FOUND_MESSAGE, reactionType));

        post.setReactions(reactions);
        return postRepository.save(post);
    }
}
