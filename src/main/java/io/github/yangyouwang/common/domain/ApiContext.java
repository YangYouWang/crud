package io.github.yangyouwang.common.domain;

/**
 * 获取userId上下文环境
 *
 * @author yangyouwang
 */
public class ApiContext {

    private static final InheritableThreadLocal<Long> userThreadLocal = new InheritableThreadLocal<>();

    public static void setUserId(Long userId) {
        userThreadLocal.set(userId);
    }

    public static Long getUserId() {
        return userThreadLocal.get();
    }

    /**
     * 删除当前登录用户方法  在拦截器方法执行后 移除当前用户对象
     */
    public static void remove() {
        userThreadLocal.remove();
    }
}