package com.nti.socialmediaappcore.util.identity;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class IdsCollector {
    public static <T extends WithUserIds> Set<String> collect(Collection<T> collection) {
        return collection.stream()
                .flatMap(item -> item.getUserIds().stream())
                .collect(Collectors.toSet());
    }

    private IdsCollector() {
    }
}
