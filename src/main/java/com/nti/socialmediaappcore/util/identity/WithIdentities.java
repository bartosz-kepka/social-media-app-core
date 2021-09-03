package com.nti.socialmediaappcore.util.identity;

import com.nti.socialmediaappcore.model.UserIdentity;
import lombok.Data;

import java.util.Set;

@Data
public class WithIdentities<T> {
    private T data;
    private Set<UserIdentity> userIdentities;

    public WithIdentities(T data, Set<UserIdentity> userIdentities) {
        this.data = data;
        this.userIdentities = userIdentities;
    }
}
