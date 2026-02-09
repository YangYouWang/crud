package io.github.yangyouwang.framework.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Description: Exception thrown when login is blocked due to rate limiting<br/>
 * date: 2026/02/09<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
public class RateLimitBlockedException extends AuthenticationException {

    public RateLimitBlockedException(String msg) {
        super(msg);
    }

    public RateLimitBlockedException(String msg, Throwable t) {
        super(msg, t);
    }
}