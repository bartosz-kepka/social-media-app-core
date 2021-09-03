package com.nti.socialmediaappcore.util.identity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

public interface WithUserIds {
    @JsonIgnore
    Set<String> getUserIds();
}
