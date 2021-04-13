package com.nti.socialmediaappcore.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ChatItemDTO {
    private String id;

    private String creatorId;

    private Set<String> membersIds;
}
