package io.github.yangyouwang.framework.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Description: 自定义扫描类<br/>
 * date: 2022/7/22 15:18<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
@EnableRetry
@EnableScheduling
@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy = true)
@Configuration
@ComponentScans(
        value = {
                @ComponentScan(basePackages = "io.github.yangyouwang")
        }
)
public class ScanConfig {
}
