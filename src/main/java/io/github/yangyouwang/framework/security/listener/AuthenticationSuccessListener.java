package io.github.yangyouwang.framework.security.listener;

import cn.hutool.extra.servlet.ServletUtil;
import io.github.yangyouwang.module.system.entity.SysLoginLog;
import io.github.yangyouwang.module.system.mapper.SysLoginLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * Description: 用户登录成功监听器事件<br/>
 * date: 2022/8/29 21:01<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
@Slf4j
@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Resource
    private SysLoginLogMapper sysLoginLogMapper;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        // 登录账号
        User user = (User) event.getAuthentication().getPrincipal();
        // 请求IP
        String ip = ServletUtil.getClientIP(((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest(), "");
        SysLoginLog sysLoginLog = new SysLoginLog();
        sysLoginLog.setAccount(user.getUsername());
        sysLoginLog.setLoginIp(ip);
        sysLoginLog.setRemark("登录成功");
        sysLoginLog.setCreateBy(user.getUsername());
        sysLoginLog.setCreateTime(new Date());
        sysLoginLogMapper.insert(sysLoginLog);
    }
}
