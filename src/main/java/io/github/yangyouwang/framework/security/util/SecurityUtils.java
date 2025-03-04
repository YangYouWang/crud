package io.github.yangyouwang.framework.security.util;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * @author yangyouwang
 * @title: SecurityUtils
 * @projectName crud
 * @description: Security工具类
 * @date 2021/3/312:34 PM
 */
public class SecurityUtils {

    /**
     * 获取当前登录用户名称
     * @return 用户名称
     */
    public static String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication)) {
            User user = (User) authentication.getPrincipal();
            return user.getUsername();
        }
        return null;
    }
}
