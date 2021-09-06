package com.nti.socialmediaappcore.service;

import com.nti.socialmediaappcore.dto.NewPostDTO;
import com.nti.socialmediaappcore.dto.NewReactionDTO;
import com.nti.socialmediaappcore.dto.PageDTO;
import com.nti.socialmediaappcore.dto.socket.SocketNewReactionDTO;
import com.nti.socialmediaappcore.exception.NotFoundException;
import com.nti.socialmediaappcore.model.*;
import com.nti.socialmediaappcore.repository.PostRepository;
import com.nti.socialmediaappcore.util.identity.IdsCollector;
import com.nti.socialmediaappcore.util.identity.WithIdentities;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    private final UserService userService;

    private final SocketIOService socketIOService;

    private static final String NOT_FOUND_MESSAGE = "Post with id \"{0}\" doesn't exist";

    public Post addPost(NewPostDTO newPostDTO) {
        String creatorId = userService.getCurrentUserId();

        Post post = new Post(newPostDTO);
        post.setDate(LocalDateTime.now(Clock.systemUTC()));
        post.setReactions(new HashMap<>());
        post.setCreatorId(creatorId);

        return postRepository.insert(post);
    }

    public Post getPost(String postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                        new NotFoundException(MessageFormat.format(NOT_FOUND_MESSAGE, postId))
                                                          );
    }

    public void makeReaction(String postId, NewReactionDTO reactionDTO) {
        Post post = getPost(postId);
        String senderId = userService.getCurrentUserId();

        Reaction reaction = reactionDTO.getType();
        Reaction none = Reaction.none;

        HashMap<String, Reaction> reactions = post.getReactions();

        if (!reactions.containsKey(senderId) && reaction != none) {
            //post nie ma reakcji, przekazany typ reakcji inny niz none
            reactions.put(senderId, reaction);
        } else if (reactions.get(senderId) != none && reactions.get(senderId) != reaction && reaction != none) {
            //jest like dajemy dislike i odwrotnie
            reactions.replace(senderId, reaction);
        } else if (reactions.containsKey(senderId) && !reactions.get(senderId).equals(none)) {
            //post ma reakcje, cofniecie reakcji
            reactions.remove(senderId, reaction);
        } else throw new NotFoundException(MessageFormat.format(NOT_FOUND_MESSAGE, reaction));

        post.setReactions(reactions);
        postRepository.save(post);

        SocketNewReactionDTO newReactionDTO = new SocketNewReactionDTO(postId, reaction, senderId);
        Set<UserIdentity> userIdentities =
                userService.getUserIdentitiesByIds(newReactionDTO.getUserIds());
        WithIdentities<SocketNewReactionDTO> socketNewReactionDTO =
                new WithIdentities<>(newReactionDTO, userIdentities);
        this.socketIOService.emitNewReaction(socketNewReactionDTO);
    }

    public WithIdentities<PageDTO<Post>> getAllPostsPage(int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByOrderByDateDesc(paging);
        Set<String> allUsersIds = IdsCollector.collect(posts.getContent());
        Set<UserIdentity> userIdentities = userService.getUserIdentitiesByIds(allUsersIds);
        return new WithIdentities<>(new PageDTO<>(posts), userIdentities);
    }
}
