package io.github.yangyouwang.framework.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description: Login rate limit configuration properties<br/>
 * date: 2026/02/09<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
@Component
@ConfigurationProperties(prefix = "login.rate.limit")
public class LoginRateLimitProperties {

    /**
     * Whether rate limiting is enabled
     */
    private boolean enabled = false;

    /**
     * Maximum failed attempts before blocking (default: 5)
     */
    private int maxAttempts = 5;

    /**
     * Duration to block after max attempts in minutes (default: 30)
     */
    private int blockDurationMinutes = 30;

    /**
     * Storage backend type: redis or hashmap (default: redis)
     */
    private String storageType = "redis";

    /**
     * Blocking mode: ip or account (default: ip)
     */
    private String mode = "ip";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public int getBlockDurationMinutes() {
        return blockDurationMinutes;
    }

    public void setBlockDurationMinutes(int blockDurationMinutes) {
        this.blockDurationMinutes = blockDurationMinutes;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}