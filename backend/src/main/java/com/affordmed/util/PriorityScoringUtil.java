package com.affordmed.util;

import java.time.Duration;
import java.time.Instant;

import com.affordmed.entity.NotificationType;

public final class PriorityScoringUtil {
    private PriorityScoringUtil() {
    }

    public static int calculateScore(NotificationType type, Instant createdAt) {
        int baseScore = switch (type) {
            case PLACEMENT -> 3000;
            case RESULT -> 2000;
            case EVENT -> 1000;
        };
        long minutes = Duration.between(createdAt, Instant.now()).toMinutes();
        int recencyScore = (int) Math.max(0, 1000 - minutes);
        return baseScore + recencyScore;
    }
}
