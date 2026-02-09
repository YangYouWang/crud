package io.github.yangyouwang.framework.security.listener;

import cn.hutool.extra.servlet.ServletUtil;
import io.github.yangyouwang.framework.config.properties.LoginRateLimitProperties;
import io.github.yangyouwang.framework.exception.RateLimitBlockedException;
import io.github.yangyouwang.framework.ratelimit.LoginRateLimiterService;
import io.github.yangyouwang.framework.util.StringUtil;
import io.github.yangyouwang.module.system.entity.SysLoginLog;
import io.github.yangyouwang.module.system.mapper.SysLoginLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * Description: 用户登录失败监听器事件<br/>
 * date: 2022/8/29 20:51<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
@Slf4j
@Component
public class AuthenticationFailureListener implements ApplicationListener<AbstractAuthenticationFailureEvent>  {

    @Resource
    private SysLoginLogMapper sysLoginLogMapper;

    @Resource
    private LoginRateLimiterService loginRateLimiterService;

    @Resource
    private LoginRateLimitProperties loginRateLimitProperties;

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        // Get username from authentication
        String username = event.getAuthentication().getPrincipal().toString();

        // Get client IP
        String ip = ServletUtil.getClientIP(((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest(), "");

        // Check if rate limiting is enabled
        if (loginRateLimitProperties.isEnabled()) {
            // Determine the key based on mode (IP or account)
            String key = "account".equals(loginRateLimitProperties.getMode()) ? username : ip;

            // Check if already blocked first
            if (loginRateLimiterService.isBlocked(key)) {
                // If already blocked, just record the login attempt in the database
                SysLoginLog sysLoginLog = new SysLoginLog();
                sysLoginLog.setAccount(username);
                sysLoginLog.setLoginIp(ip);
                sysLoginLog.setRemark("Login blocked due to rate limiting");
                sysLoginLog.setCreateBy(username);
                sysLoginLog.setCreateTime(new Date());
                sysLoginLogMapper.insert(sysLoginLog);

                // Throw a specific exception for blocked users
                throw new RateLimitBlockedException("Too many failed login attempts. Please try again later.");
            }

            // Get remaining attempts before recording this one
            int remainingBefore = loginRateLimiterService.getRemainingAttempts(key);

            // Record the failed attempt
            loginRateLimiterService.recordFailedAttempt(key);

            // Check if this attempt just caused the user to be blocked
            if (loginRateLimiterService.isBlocked(key)) {
                // User is now blocked due to this failed attempt

                // Store information in request for the failure handler to use
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                request.setAttribute("rateLimitJustBlocked", true);
                request.setAttribute("rateLimitRemainingBefore", remainingBefore);

                // Record in DB
                SysLoginLog sysLoginLog = new SysLoginLog();
                sysLoginLog.setAccount(username);
                sysLoginLog.setLoginIp(ip);
                sysLoginLog.setRemark("Login blocked due to rate limiting after too many failed attempts");
                sysLoginLog.setCreateBy(username);
                sysLoginLog.setCreateTime(new Date());
                sysLoginLogMapper.insert(sysLoginLog);
            } else {
                // Store information about remaining attempts for the failure handler
                int remainingAfter = loginRateLimiterService.getRemainingAttempts(key);
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                request.setAttribute("rateLimitRemainingAfter", remainingAfter);
            }
        }

        // Log the login failure regardless of rate limiting
        String message = StringUtil.getAuthenticationFailureMessage(event);
        SysLoginLog sysLoginLog = new SysLoginLog();
        sysLoginLog.setAccount(username);
        sysLoginLog.setLoginIp(ip);
        sysLoginLog.setRemark(message);
        sysLoginLog.setCreateBy(username);
        sysLoginLog.setCreateTime(new Date());
        sysLoginLogMapper.insert(sysLoginLog);
    }
}
