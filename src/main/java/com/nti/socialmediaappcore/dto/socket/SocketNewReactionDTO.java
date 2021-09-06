package com.nti.socialmediaappcore.dto.socket;

import com.nti.socialmediaappcore.model.Reaction;
import com.nti.socialmediaappcore.util.identity.WithUserIds;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocketNewReactionDTO implements WithUserIds {
    private String postId;
    private Reaction reaction;
    private String senderId;

    @Override
    public Set<String> getUserIds() {
        Set<String> usersIds = new HashSet<>();
        usersIds.add(this.senderId);
        return usersIds;
    }
}
