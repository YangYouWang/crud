package io.github.yangyouwang.framework.ratelimit.impl;

import io.github.yangyouwang.framework.config.properties.LoginRateLimitProperties;
import io.github.yangyouwang.framework.ratelimit.LoginRateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: HashMap-based login rate limiter implementation<br/>
 * date: 2026/02/09<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
@Service("hashMapLoginRateLimiterService")
public class HashMapLoginRateLimiterServiceImpl implements LoginRateLimiterService {

    @Autowired
    private LoginRateLimitProperties properties;

    private final Map<String, AttemptInfo> attemptsMap = new ConcurrentHashMap<>();

    // Inner class to hold attempt information
    private static class AttemptInfo {
        private int attempts;
        private LocalDateTime lastAttemptTime;
        private LocalDateTime blockExpiryTime;

        public AttemptInfo(int attempts, LocalDateTime lastAttemptTime, LocalDateTime blockExpiryTime) {
            this.attempts = attempts;
            this.lastAttemptTime = lastAttemptTime;
            this.blockExpiryTime = blockExpiryTime;
        }

        public int getAttempts() { return attempts; }
        public void setAttempts(int attempts) { this.attempts = attempts; }
        public LocalDateTime getLastAttemptTime() { return lastAttemptTime; }
        public void setLastAttemptTime(LocalDateTime lastAttemptTime) { this.lastAttemptTime = lastAttemptTime; }
        public LocalDateTime getBlockExpiryTime() { return blockExpiryTime; }
        public void setBlockExpiryTime(LocalDateTime blockExpiryTime) { this.blockExpiryTime = blockExpiryTime; }

        public boolean isBlocked() {
            return blockExpiryTime != null && blockExpiryTime.isAfter(LocalDateTime.now());
        }
    }

    @Override
    public boolean isBlocked(String key) {
        AttemptInfo info = attemptsMap.get(key);
        if (info == null) {
            return false;
        }

        // Check if currently blocked
        if (info.isBlocked()) {
            return true;
        }

        // Clean up expired entries
        cleanupExpiredEntries();

        return false;
    }

    @Override
    public void recordFailedAttempt(String key) {
        synchronized (this) {
            AttemptInfo info = attemptsMap.get(key);

            if (info == null) {
                info = new AttemptInfo(1, LocalDateTime.now(), null);
                attemptsMap.put(key, info);
            } else {
                // Check if currently blocked
                if (info.isBlocked()) {
                    return; // Already blocked, don't increment counter
                }

                info.setAttempts(info.getAttempts() + 1);
                info.setLastAttemptTime(LocalDateTime.now());

                // Check if max attempts reached
                if (info.getAttempts() >= properties.getMaxAttempts()) {
                    // Block the key
                    LocalDateTime blockExpiry = LocalDateTime.now().plusMinutes(properties.getBlockDurationMinutes());
                    info.setBlockExpiryTime(blockExpiry);
                }
            }
        }
    }

    @Override
    public void clearAttempts(String key) {
        attemptsMap.remove(key);
    }

    @Override
    public int getRemainingAttempts(String key) {
        AttemptInfo info = attemptsMap.get(key);

        if (info == null) {
            return properties.getMaxAttempts(); // No attempts yet
        }

        // Check if currently blocked
        if (info.isBlocked()) {
            return -1; // Already blocked
        }

        return Math.max(0, properties.getMaxAttempts() - info.getAttempts());
    }

    @PostConstruct
    private void scheduleCleanup() {
        // Schedule periodic cleanup of expired entries
        Thread cleanupThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(5 * 60 * 1000); // Clean up every 5 minutes
                    cleanupExpiredEntries();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        cleanupThread.setDaemon(true);
        cleanupThread.start();
    }

    private void cleanupExpiredEntries() {
        LocalDateTime now = LocalDateTime.now();
        attemptsMap.entrySet().removeIf(entry -> {
            AttemptInfo info = entry.getValue();
            // Remove if both attempt window and block time have expired
            boolean shouldRemove = true;

            // Check if blocked - don't remove if still blocked
            if (info.isBlocked()) {
                shouldRemove = false;
            } else {
                // If not blocked, remove if last attempt was long ago
                // Keep entries for the block duration period to maintain state
                LocalDateTime checkTime = now.minusMinutes(properties.getBlockDurationMinutes());
                if (info.getLastAttemptTime() != null && info.getLastAttemptTime().isAfter(checkTime)) {
                    shouldRemove = false;
                }
            }

            return shouldRemove;
        });
    }
}