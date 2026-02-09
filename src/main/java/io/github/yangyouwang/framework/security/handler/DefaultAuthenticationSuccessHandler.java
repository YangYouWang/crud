package io.github.yangyouwang.framework.security.handler;

import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import io.github.yangyouwang.common.base.domain.Result;
import io.github.yangyouwang.framework.config.properties.LoginRateLimitProperties;
import io.github.yangyouwang.framework.ratelimit.LoginRateLimiterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * 1.自定义的登陆成功处理  implements  AuthenticationSuccessHandler  Override  onAuthenticationSuccess()
 * 2. 或者继承框架默认实现的成功处理器类 SavedRequestAwareAuthenticationSuccessHandler 重写父类方法onAuthenticationSuccess
 * @author yangyouwang
 */
@Component
public class DefaultAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAuthenticationSuccessHandler.class);

    @Autowired
    private LoginRateLimiterService loginRateLimiterService;

    @Autowired
    private LoginRateLimitProperties loginRateLimitProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // Clear rate limit attempts on successful login
        if (loginRateLimitProperties.isEnabled()) {
            String username = authentication.getName();
            String ip = ServletUtil.getClientIP(request, "");

            // Determine the key based on mode (IP or account)
            String key = "account".equals(loginRateLimitProperties.getMode()) ? username : ip;

            // Clear the attempts for the key
            loginRateLimiterService.clearAttempts(key);
        }

        LOGGER.info("----login in success----");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(Result.success()));
        writer.flush();
    }
}