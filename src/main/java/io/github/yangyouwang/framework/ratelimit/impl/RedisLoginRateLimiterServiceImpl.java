package io.github.yangyouwang.framework.ratelimit.impl;

import io.github.yangyouwang.framework.config.properties.LoginRateLimitProperties;
import io.github.yangyouwang.framework.ratelimit.LoginRateLimiterService;
import io.github.yangyouwang.framework.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: Redis-based login rate limiter implementation<br/>
 * date: 2026/02/09<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
@Service("redisLoginRateLimiterService")
public class RedisLoginRateLimiterServiceImpl implements LoginRateLimiterService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private LoginRateLimitProperties properties;

    private static final String LOGIN_BLOCK_KEY_PREFIX = "login:block:";
    private static final String LOGIN_ATTEMPT_KEY_PREFIX = "login:attempt:";

    @Override
    public boolean isBlocked(String key) {
        String blockKey = LOGIN_BLOCK_KEY_PREFIX + key;
        return redisUtil.hasKey(blockKey);
    }

    @Override
    public void recordFailedAttempt(String key) {
        String attemptKey = LOGIN_ATTEMPT_KEY_PREFIX + key;

        // Get current attempt count
        Object currentCountObj = redisUtil.get(attemptKey);
        int currentCount = currentCountObj instanceof Integer ? (Integer) currentCountObj : 0;

        // Increment attempt count
        currentCount++;

        // Check if max attempts reached
        if (currentCount >= properties.getMaxAttempts()) {
            // Block the key
            String blockKey = LOGIN_BLOCK_KEY_PREFIX + key;
            redisUtil.set(blockKey, "blocked", properties.getBlockDurationMinutes() * 60); // Convert minutes to seconds
        } else {
            // Set the new attempt count with the same duration as block duration (convert minutes to seconds)
            redisUtil.set(attemptKey, currentCount, properties.getBlockDurationMinutes() * 60);
        }
    }

    @Override
    public void clearAttempts(String key) {
        String attemptKey = LOGIN_ATTEMPT_KEY_PREFIX + key;
        String blockKey = LOGIN_BLOCK_KEY_PREFIX + key;

        // Remove both attempt and block keys
        redisUtil.del(attemptKey, blockKey);
    }

    @Override
    public int getRemainingAttempts(String key) {
        String blockKey = LOGIN_BLOCK_KEY_PREFIX + key;

        // Check if already blocked
        if (redisUtil.hasKey(blockKey)) {
            return -1; // Already blocked
        }

        String attemptKey = LOGIN_ATTEMPT_KEY_PREFIX + key;
        Object currentCountObj = redisUtil.get(attemptKey);
        int currentCount = currentCountObj instanceof Integer ? (Integer) currentCountObj : 0;

        return Math.max(0, properties.getMaxAttempts() - currentCount);
    }
}