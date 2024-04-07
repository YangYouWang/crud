package io.github.yangyouwang.framework.security.listener;

import cn.hutool.extra.servlet.ServletUtil;
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

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        // 登录账号
        String username = event.getAuthentication().getPrincipal().toString();
        // 登录失败原因
        String message = StringUtil.getAuthenticationFailureMessage(event);
        // 请求IP
        String ip = ServletUtil.getClientIP(((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest(), "");
        SysLoginLog sysLoginLog = new SysLoginLog();
        sysLoginLog.setAccount(username);
        sysLoginLog.setLoginIp(ip);
        sysLoginLog.setRemark(message);
        sysLoginLog.setCreateBy(username);
        sysLoginLog.setCreateTime(new Date());
        sysLoginLogMapper.insert(sysLoginLog);
    }
}
