package com.nti.socialmediaappcore.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ChatItemDTO {
    private String id;

    private Set<String> membersIds;
}
