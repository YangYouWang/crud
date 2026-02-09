package io.github.yangyouwang.framework.ratelimit;

import io.github.yangyouwang.framework.config.properties.LoginRateLimitProperties;
import io.github.yangyouwang.framework.ratelimit.impl.HashMapLoginRateLimiterServiceImpl;
import io.github.yangyouwang.framework.ratelimit.impl.RedisLoginRateLimiterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Description: Primary login rate limiter service that delegates to the configured implementation<br/>
 * date: 2026/02/09<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
@Service
@Primary
public class LoginRateLimiterServiceImpl implements LoginRateLimiterService {

    @Autowired
    private LoginRateLimitProperties properties;

    @Autowired
    @Qualifier("redisLoginRateLimiterService")
    private RedisLoginRateLimiterServiceImpl redisLoginRateLimiterService;

    @Autowired
    @Qualifier("hashMapLoginRateLimiterService")
    private HashMapLoginRateLimiterServiceImpl hashMapLoginRateLimiterService;

    private LoginRateLimiterService getActiveService() {
        if ("redis".equalsIgnoreCase(properties.getStorageType())) {
            return redisLoginRateLimiterService;
        } else {
            return hashMapLoginRateLimiterService;
        }
    }

    @Override
    public boolean isBlocked(String key) {
        return getActiveService().isBlocked(key);
    }

    @Override
    public void recordFailedAttempt(String key) {
        getActiveService().recordFailedAttempt(key);
    }

    @Override
    public void clearAttempts(String key) {
        getActiveService().clearAttempts(key);
    }

    @Override
    public int getRemainingAttempts(String key) {
        return getActiveService().getRemainingAttempts(key);
    }
}