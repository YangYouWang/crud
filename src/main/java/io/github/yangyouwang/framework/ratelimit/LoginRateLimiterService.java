package io.github.yangyouwang.framework.ratelimit;

/**
 * Description: Login rate limiter service interface<br/>
 * date: 2026/02/09<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
public interface LoginRateLimiterService {

    /**
     * Check if the given key is blocked
     * @param key the key to check (IP address or username)
     * @return true if blocked, false otherwise
     */
    boolean isBlocked(String key);

    /**
     * Record a failed login attempt
     * @param key the key to record attempt for (IP address or username)
     */
    void recordFailedAttempt(String key);

    /**
     * Clear attempts for a given key (e.g., after successful login)
     * @param key the key to clear attempts for
     */
    void clearAttempts(String key);

    /**
     * Get remaining attempts before blocking
     * @param key the key to check remaining attempts for
     * @return number of remaining attempts before blocking, -1 if already blocked
     */
    int getRemainingAttempts(String key);
}