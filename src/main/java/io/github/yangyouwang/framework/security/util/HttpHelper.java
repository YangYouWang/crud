package io.github.yangyouwang.framework.security.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 通用http工具封装
 *
 * @author tecsun
 */
public class HttpHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHelper.class);

    public static String getBodyString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try (InputStream inputStream = request.getInputStream();
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            LOGGER.warn("getBodyString出现问题！");
        }
        return sb.toString();
    }


    /**
     * 获取请求ip
     *
     * @param request
     * @return
     */
    public static String getIP(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String headerIP = request.getHeader("x-real-ip");
        if (StrUtil.isEmpty(headerIP) || "null".equals(headerIP)) {
            headerIP = request.getHeader("x-forwarded-for");
        }
        if (StrUtil.isEmpty(headerIP) && !"null".equals(headerIP)) {
            ip = headerIP;
        }
        return ip;
    }

    /**
     * 获取真实的ip
     *
     * @param request
     * @return
     * @throws
     */
    public static String getRealIp(HttpServletRequest request) {
        String ip;
        // 有的user可能使用代理，为处理用户使用代理的情况，使用x-forwarded-for
        if (request.getHeader("x-forwarded-for") == null) {
            ip = request.getRemoteAddr();
        } else {
            ip = request.getHeader("x-forwarded-for");
        }
        if ("127.0.0.1".equals(ip)) {
            try {
                // 获取本机真正的ip地址
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

    /**
     * 查找指定字符串是否匹配指定字符串列表中的任意一个字符串
     *
     * @param str  指定字符串
     * @param strs 需要检查的字符串数组
     * @return 是否匹配
     */
    public static boolean matches(String str, List<String> strs) {
        if (StrUtil.isEmpty(str) || CollUtil.isEmpty(strs)) {
            return false;
        }
        for (String pattern : strs) {
            if (isMatch(pattern, str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断url是否与规则配置:
     * ? 表示单个字符;
     * * 表示一层路径内的任意字符串，不可跨层级;
     * ** 表示任意层路径;
     *
     * @param pattern 匹配规则
     * @param url     需要匹配的url
     * @return
     */
    public static boolean isMatch(String pattern, String url) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, url);
    }
}
