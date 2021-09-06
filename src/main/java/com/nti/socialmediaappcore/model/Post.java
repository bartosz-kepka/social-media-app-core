package com.nti.socialmediaappcore.model;

import com.nti.socialmediaappcore.dto.NewPostDTO;
import com.nti.socialmediaappcore.util.identity.WithUserIds;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "posts")
public class Post implements WithUserIds {
    @Id
    private String id;

    private String content;

    private HashMap<String, Reaction> reactions;

    private String creatorId;

    private LocalDateTime date;

    public Post(NewPostDTO newPostDTO) {
        this.content = newPostDTO.getContent();
    }

    @Override
    public Set<String> getUserIds() {
        Set<String> usersIds = new HashSet<>();
        usersIds.add(creatorId);
        usersIds.addAll(reactions.keySet());
        return usersIds;
    }
}
