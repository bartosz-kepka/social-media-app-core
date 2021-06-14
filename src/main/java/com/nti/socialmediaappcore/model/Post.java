package com.nti.socialmediaappcore.model;

import com.nti.socialmediaappcore.dto.NewReactionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "posts")
public class Post {
    @Id
    private String id;

    private String content;

    private HashMap<String, Reactions.reaction> reactions;
}
