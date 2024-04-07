package io.github.yangyouwang.framework.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * Description: 服务配置类 <br/>
 * date: 2022/8/18 9:50<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
@Slf4j
@Component
public class ServerConfig implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 端口
     */
    @Value("${server.port}")
    private String port;

    @SneakyThrows
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String url = "http://" + InetAddress.getLocalHost().getHostAddress() +":" + port;
        log.info("\n----------------------------------------------------------\n\t" +
                "Application is running! Access URLs:\n\t" +
                "访问网址:"+ url + "\n" +
                "----------------------------------------------------------");
    }
}
