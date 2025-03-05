package io.github.yangyouwang.framework.security.filter;

import com.alibaba.fastjson.JSON;
import io.github.yangyouwang.common.constant.ConfigConsts;
import io.github.yangyouwang.common.base.domain.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @author yangyouwang
 * @title: ValidateCodeFilter
 * @projectName crud
 * @description: 验证码过滤器
 * @date 2021/5/809:17 PM
 */
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 登陆请求
        if (StringUtils.equals("/login", request.getServletPath())
                && StringUtils.equalsIgnoreCase(request.getMethod(), "POST")) {
            try {
                validate(request);
            } catch (Exception e) {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(Result.failure(e.getMessage())));
                writer.flush();
                return;
            }
        }
        // 不是一个登录请求，不做校验 直接通过
        filterChain.doFilter(request, response);
    }

    /**
     * 验证码校验
     */
    private void validate(HttpServletRequest request) {
        String code = request.getParameter("code");
        if (StringUtils.isBlank(code)) {
            throw new RuntimeException("验证码不能为空");
        }
        Object checkCode = request.getSession(false).getAttribute(ConfigConsts.IMAGE_CODE_SESSION);
        if (Objects.isNull(checkCode)) {
            throw new RuntimeException("验证码不存在");
        }
        if (!StringUtils.equalsIgnoreCase(code,checkCode.toString())) {
            throw new RuntimeException("验证码不匹配");
        }
        request.getSession(false).removeAttribute(ConfigConsts.IMAGE_CODE_SESSION);
    }
}
