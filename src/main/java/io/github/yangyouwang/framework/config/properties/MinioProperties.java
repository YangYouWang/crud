package io.github.yangyouwang.framework.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * minio 配置文件
 * 
 * @author yangyouwang
 */
@Data
@Component
@ConfigurationProperties(prefix = MinioProperties.PREFIX)
public class MinioProperties
{
    public static final String PREFIX = "minio";
    /**
     * ip：minio地址，分布式节点情况下推荐配置一个nginx路由，转接给nginx的负载均衡
     */
    private String endpoint;

    /**
     * 账号
     */
    private String accessKey;

    /**
     * 秘钥
     */
    private String secretKey;
    /**
     * bucketName
     */
    private String bucketName;
}
