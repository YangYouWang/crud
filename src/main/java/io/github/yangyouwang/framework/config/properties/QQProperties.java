package io.github.yangyouwang.framework.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * QQ配置
 * @author yangyouwang
 */
@Data
@Component
@ConfigurationProperties(prefix = QQProperties.PREFIX)
public class QQProperties {

    public static final String PREFIX = "qq";
    /**
     * QQ appID
     */
    private String appID;
    /**
     * QQ appSecret
     */
    private String appSecret;
    /**
     * 回调地址
     */
    private String redirectUrl;
}