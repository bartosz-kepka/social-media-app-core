package com.nti.socialmediaappcore.model;

import com.nti.socialmediaappcore.util.identity.WithUserIds;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chats")
public class Chat implements WithUserIds {
    @Id
    private String id;

    private String creatorId;

    private Set<String> membersIds;

    private List<Message> messages = new ArrayList<>();

    public Chat(String creatorId,
                Set<String> membersIds) {
        this.creatorId = creatorId;
        this.membersIds = membersIds;
    }

    @Override
    public Set<String> getUserIds() {
        return membersIds;
    }
}
