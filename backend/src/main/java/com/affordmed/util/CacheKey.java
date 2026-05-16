package com.affordmed.util;

import org.springframework.data.domain.Pageable;

public final class CacheKey {
    private CacheKey() {
    }

    public static String forNotifications(String studentId, Object type, Object read, Pageable pageable) {
        return studentId + ":" + type + ":" + read + ":" + pageable.getPageNumber() + ":" + pageable.getPageSize() + ":" + pageable.getSort();
    }
}
