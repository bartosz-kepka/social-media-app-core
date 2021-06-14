package com.nti.socialmediaappcore.dto;

import com.nti.socialmediaappcore.model.Reactions;
import lombok.Data;


@Data
public class NewReactionDTO {

    private String senderId;
    private Reactions.reaction type;

}
