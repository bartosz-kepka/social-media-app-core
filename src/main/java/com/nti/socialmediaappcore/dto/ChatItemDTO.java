package com.nti.socialmediaappcore.dto;

import com.nti.socialmediaappcore.model.Chat;
import com.nti.socialmediaappcore.util.identity.WithUserIds;
import lombok.Data;

import java.util.Set;

@Data
public class ChatItemDTO implements WithUserIds {
    private String id;

    private String creatorId;

    private Set<String> membersIds;

    public ChatItemDTO(Chat chat) {
        this.id = chat.getId();
        this.creatorId = chat.getCreatorId();
        this.membersIds = chat.getMembersIds();
    }

    @Override
    public Set<String> getUserIds() {
        return membersIds;
    }
}
