package com.nti.socialmediaappcore.dto.socket;

import com.nti.socialmediaappcore.model.Reactions;

import java.util.HashMap;

public class SocketPostReactionDTO {
    private String postId;
    private HashMap<String, Reactions.reaction> reactions;

    public SocketPostReactionDTO(String postId, HashMap<String, Reactions.reaction> reactions){
        this.postId = postId;
        this.reactions = reactions;
    }
}
