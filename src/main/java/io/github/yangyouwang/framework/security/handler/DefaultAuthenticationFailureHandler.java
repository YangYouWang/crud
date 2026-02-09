package io.github.yangyouwang.framework.security.handler;

import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import io.github.yangyouwang.common.base.domain.Result;
import io.github.yangyouwang.framework.config.properties.LoginRateLimitProperties;
import io.github.yangyouwang.framework.exception.RateLimitBlockedException;
import io.github.yangyouwang.framework.ratelimit.LoginRateLimiterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录失败处理器
 * @author yangyouwang
 */
@Component
public class DefaultAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAuthenticationFailureHandler.class);

    @Autowired
    private LoginRateLimiterService loginRateLimiterService;

    @Autowired
    private LoginRateLimitProperties loginRateLimitProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        // 特殊处理限流被阻止的异常
        if (exception instanceof RateLimitBlockedException) {
            String username = request.getParameter("userName"); // 获取用户名
            String ip = ServletUtil.getClientIP(request, ""); // 获取客户端IP

            LOGGER.warn("由于限流导致登录被阻止，来自IP: " + ip + ", 用户名: " + username);
            response.setStatus(429); // 429 请求过多
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(Result.failure("登录失败次数过多。账户/IP 已被临时阻止 " +
                loginRateLimitProperties.getBlockDurationMinutes() + " 分钟。请稍后再试。")));
            writer.flush();
            return;
        }

        String username = request.getParameter("userName"); // 获取用户名
        String ip = ServletUtil.getClientIP(request, ""); // 获取客户端IP

        // 检查是否启用限流
        if (loginRateLimitProperties.isEnabled()) {
            // 根据模式确定键值（IP或账户）
            String key = "account".equals(loginRateLimitProperties.getMode()) ? username : ip;

            // 检查用户是否刚因本次尝试而被阻止（如记录在监听器中）
            Boolean justBlocked = (Boolean) request.getAttribute("rateLimitJustBlocked");
            if (justBlocked != null && justBlocked) {
                // 用户因本次尝试刚刚被阻止
                Integer remainingBefore = (Integer) request.getAttribute("rateLimitRemainingBefore");

                LOGGER.info("登录失败: 凭证无效，用户现在因 " + (loginRateLimitProperties.getMaxAttempts() - remainingBefore) + " 次尝试而被阻止");
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(Result.failure("凭证无效。登录失败次数过多。您的账户/IP 现在已被阻止 " +
                    loginRateLimitProperties.getBlockDurationMinutes() + " 分钟。")));
                writer.flush();
                return;
            }

            // 检查我们是否有关于剩余尝试的信息
            Integer remainingAfter = (Integer) request.getAttribute("rateLimitRemainingAfter");
            if (remainingAfter != null) {
                LOGGER.info("登录失败 : " + exception.getMessage() + ", 剩余尝试次数: " + remainingAfter);

                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();

                // 显示凭证无效以及剩余尝试次数
                writer.write(JSON.toJSONString(Result.failure("用户名或密码错误。还有 " + remainingAfter + " 次尝试机会。")));
                writer.flush();
                return;
            }
        }

        // 如果未启用限流或没有特殊状态，则使用原始行为
        LOGGER.info("登录失败 : " + exception.getMessage());
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(Result.failure("用户名或密码错误")));
        writer.flush();
    }
}