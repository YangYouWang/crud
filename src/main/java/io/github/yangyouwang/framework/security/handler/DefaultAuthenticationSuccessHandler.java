package io.github.yangyouwang.framework.security.handler;

import com.alibaba.fastjson.JSON;
import io.github.yangyouwang.common.base.domain.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 1.自定义的登陆成功处理  implements  AuthenticationSuccessHandler  Override  onAuthenticationSuccess()
 * 2. 或者继承框架默认实现的成功处理器类 SavedRequestAwareAuthenticationSuccessHandler 重写父类方法onAuthenticationSuccess
 * @author yangyouwang
 */
@Component
public class DefaultAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        LOGGER.info("----login in succcess----");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(Result.success()));
        writer.flush();
    }
}