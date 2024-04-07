package io.github.yangyouwang.framework.util.aliyun;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import io.github.yangyouwang.framework.config.properties.VodProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

/**
 * Description: 视频点播服务 <br/>
 * date: 2022/12/10 16:50<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
@Slf4j
@Component
public class SampleVod {
    /**
     * 区域
     */
    private String regionId = "cn-shanghai";
    /**
     * 配置
     */
    private VodProperties vodProperties;
    /**
     * client
     */
    private DefaultAcsClient client;

    public SampleVod(ObjectProvider<VodProperties> vodPropertiesObjectProvider) {
        this.vodProperties =  vodPropertiesObjectProvider.getIfAvailable(VodProperties::new);
        initVod();
    }

    public void initVod() {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, vodProperties.getAccessKeyId(), vodProperties.getAccessKeySecret());
        this.client = new DefaultAcsClient(profile);
    }

    /**
     * 本地文件上传接口
     */
    public String uploadVideo(String title, String fileName, InputStream inputStream) {
        UploadStreamRequest request = new UploadStreamRequest(vodProperties.getAccessKeyId(), vodProperties.getAccessKeySecret(), title, fileName, inputStream);
        request.setApiRegionId(regionId);
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);
        return response.getVideoId();
    }

    /**
     * 获取视频播放地址
     */
    public String getPlayInfo(String videoId) {
        //初始化客户端、请求对象和相应对象
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        try {
            request.setVideoId(videoId);
            GetPlayInfoResponse response = client.getAcsResponse(request);
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            // 播放地址
            return playInfoList.stream().findFirst().get().getPlayURL();
        } catch (Exception e) {
            throw new RuntimeException("获取视频播放地址出错了：" + e.getLocalizedMessage());
        }
    }

    public CreateUploadVideoResponse getToken(String name) {
        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        request.setFileName(name);
        request.setTitle(name);
        try {
            return client.getAcsResponse(request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取上传视频凭证出错了");
        }
    }
}
