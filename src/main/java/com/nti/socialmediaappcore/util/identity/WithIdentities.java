package com.nti.socialmediaappcore.util.identity;

import com.nti.socialmediaappcore.model.UserIdentity;
import lombok.Data;

import java.util.Set;

@Data
public class WithIdentities<T> {
    private T payload;
    private Set<UserIdentity> userIdentities;

    public WithIdentities(T payload, Set<UserIdentity> userIdentities) {
        this.payload = payload;
        this.userIdentities = userIdentities;
    }
}
