package io.github.yangyouwang.framework.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * WeChat配置
 * @author yangyouwang
 */
@Data
@Component
@ConfigurationProperties(prefix = WeChatProperties.PREFIX)
public class WeChatProperties {

    public static final String PREFIX = "wechat";
    /**
     * 支付服务器的ip
     */
    private String spbillCreateIp;
    /**
     * 支付密钥
     */
    private String apiKey;
    /**
     * 商户号
     */
    private String mchId;
    /**
     * 微信小程序 appID
     */
    private String appID;
    /**
     * 微信小程序 appSecret
     */
    private String appSecret;
}