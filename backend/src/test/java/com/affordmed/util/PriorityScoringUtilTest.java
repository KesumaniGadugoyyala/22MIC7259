package com.affordmed.util;

import java.time.Instant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.affordmed.entity.NotificationType;

class PriorityScoringUtilTest {
    @Test
    void calculateScoreWeightsTypeAndRecency() {
        Instant now = Instant.now();
        int placement = PriorityScoringUtil.calculateScore(NotificationType.PLACEMENT, now);
        int result = PriorityScoringUtil.calculateScore(NotificationType.RESULT, now);
        int event = PriorityScoringUtil.calculateScore(NotificationType.EVENT, now);

        Assertions.assertTrue(placement > result);
        Assertions.assertTrue(result > event);
    }
}
