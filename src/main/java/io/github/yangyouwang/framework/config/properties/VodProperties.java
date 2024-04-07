package io.github.yangyouwang.framework.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description: 阿里云视频点播 <br/>
 * date: 2022/12/10 16:48<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
@Data
@Component
@ConfigurationProperties(prefix = VodProperties.PREFIX)
public class VodProperties {

    public static final String PREFIX = "aliyun.vod";

    /**
     * accessKeyId
     */
    private String accessKeyId;

    /**
     *   accessKeySecret
     */
    private String accessKeySecret;
}
