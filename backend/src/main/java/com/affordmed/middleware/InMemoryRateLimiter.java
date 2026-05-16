package com.affordmed.middleware;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.affordmed.config.AppProperties;

@Component
public class InMemoryRateLimiter {
    private final int capacity;
    private final int refillPerMinute;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public InMemoryRateLimiter(AppProperties properties) {
        this.capacity = properties.getRateLimit().getCapacity();
        this.refillPerMinute = properties.getRateLimit().getRefillPerMinute();
    }

    public boolean tryConsume(String key) {
        Bucket bucket = buckets.computeIfAbsent(key, ignored -> new Bucket(capacity, refillPerMinute));
        return bucket.tryConsume();
    }

    private static class Bucket {
        private final int capacity;
        private final int refillPerMinute;
        private int tokens;
        private Instant lastRefill;

        private Bucket(int capacity, int refillPerMinute) {
            this.capacity = capacity;
            this.refillPerMinute = refillPerMinute;
            this.tokens = capacity;
            this.lastRefill = Instant.now();
        }

        private synchronized boolean tryConsume() {
            refillTokens();
            if (tokens > 0) {
                tokens -= 1;
                return true;
            }
            return false;
        }

        private void refillTokens() {
            long minutes = Duration.between(lastRefill, Instant.now()).toMinutes();
            if (minutes <= 0) {
                return;
            }
            int refill = Math.toIntExact(minutes * refillPerMinute);
            tokens = Math.min(capacity, tokens + refill);
            lastRefill = Instant.now();
        }
    }
}
