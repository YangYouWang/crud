package io.github.yangyouwang.framework.util.aliyun;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import io.github.yangyouwang.framework.config.properties.OSSProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 上传文件到oss
 *
 * @author yangyouwang
 */
@Component
public class SampleOSS {
    /**
     * oss 工具客户端
     */
    private OSSClient ossClient;

    private OSSProperties ossProperties;

    public SampleOSS(ObjectProvider<OSSProperties> ossPropertiesObjectProvider) {
        this.ossProperties=ossPropertiesObjectProvider.getIfAvailable(OSSProperties::new);
        initOSS(ossProperties);
    }
    /**
     * 初始化 oss 客户端
     *
     */
    private void initOSS(OSSProperties ossProperties) {
        ossClient = new OSSClient(ossProperties.getEndPoint(),
                new DefaultCredentialProvider(ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret()),
                new ClientConfiguration());
    }
    /**
     * 上传文件至阿里云 OSS
     * 文件上传成功,返回文件完整访问路径
     * 文件上传失败,返回 null
     *
     * @param file    待上传文件
     * @param fileDir 文件保存目录
     * @return oss 中的相对文件路径
     */
    public String upload(MultipartFile file, String fileDir) {
        StringBuilder fileUrl = new StringBuilder();
        try {
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            String fileName = System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 18) + suffix;
            if (!fileDir.endsWith("/")) {
                fileDir = fileDir.concat("/");
            }
            fileUrl.append(fileDir).append(fileName);

            ossClient.putObject(ossProperties.getBucketName(), fileUrl.toString(), file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return fileUrl.insert(0, ossProperties.getUrl()).toString();
    }
}