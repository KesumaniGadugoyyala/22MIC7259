package com.affordmed.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final RateLimit rateLimit = new RateLimit();
    private final PriorityInbox priorityInbox = new PriorityInbox();
    private final Security security = new Security();
    private final Cache cache = new Cache();
    private final WebSocket webSocket = new WebSocket();
    private final Queue queue = new Queue();

    public RateLimit getRateLimit() {
        return rateLimit;
    }

    public PriorityInbox getPriorityInbox() {
        return priorityInbox;
    }

    public Security getSecurity() {
        return security;
    }

    public Cache getCache() {
        return cache;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public Queue getQueue() {
        return queue;
    }

    public static class RateLimit {
        private int capacity = 60;
        private int refillPerMinute = 60;

        public int getCapacity() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public int getRefillPerMinute() {
            return refillPerMinute;
        }

        public void setRefillPerMinute(int refillPerMinute) {
            this.refillPerMinute = refillPerMinute;
        }
    }

    public static class PriorityInbox {
        private int maxSize = 10;
        private int sampleSize = 200;

        public int getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }

        public int getSampleSize() {
            return sampleSize;
        }

        public void setSampleSize(int sampleSize) {
            this.sampleSize = sampleSize;
        }
    }

    public static class Security {
        private final MockJwt mockJwt = new MockJwt();

        public MockJwt getMockJwt() {
            return mockJwt;
        }

        public static class MockJwt {
            private boolean enabled = true;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }
        }
    }

    public static class Cache {
        private int ttlSeconds = 30;

        public int getTtlSeconds() {
            return ttlSeconds;
        }

        public void setTtlSeconds(int ttlSeconds) {
            this.ttlSeconds = ttlSeconds;
        }
    }

    public static class WebSocket {
        private String endpoint = "/ws";
        private String topicPrefix = "/topic";

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getTopicPrefix() {
            return topicPrefix;
        }

        public void setTopicPrefix(String topicPrefix) {
            this.topicPrefix = topicPrefix;
        }
    }

    public static class Queue {
        private boolean enabled = true;
        private String notificationExchange = "notifications.exchange";
        private String notificationQueue = "notifications.queue";
        private String notificationRoutingKey = "notifications.bulk";
        private String notificationDlq = "notifications.dlq";
        private String notificationDlqRoutingKey = "notifications.dead";

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getNotificationExchange() {
            return notificationExchange;
        }

        public void setNotificationExchange(String notificationExchange) {
            this.notificationExchange = notificationExchange;
        }

        public String getNotificationQueue() {
            return notificationQueue;
        }

        public void setNotificationQueue(String notificationQueue) {
            this.notificationQueue = notificationQueue;
        }

        public String getNotificationRoutingKey() {
            return notificationRoutingKey;
        }

        public void setNotificationRoutingKey(String notificationRoutingKey) {
            this.notificationRoutingKey = notificationRoutingKey;
        }

        public String getNotificationDlq() {
            return notificationDlq;
        }

        public void setNotificationDlq(String notificationDlq) {
            this.notificationDlq = notificationDlq;
        }

        public String getNotificationDlqRoutingKey() {
            return notificationDlqRoutingKey;
        }

        public void setNotificationDlqRoutingKey(String notificationDlqRoutingKey) {
            this.notificationDlqRoutingKey = notificationDlqRoutingKey;
        }
    }
}
