package io.github.yangyouwang.core.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import io.github.yangyouwang.common.enums.ResultStatus;
import io.github.yangyouwang.core.exception.CrudException;
import io.github.yangyouwang.core.properties.SmsProperties;
import io.github.yangyouwang.core.util.SpringUtils;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yangyouwang
 * @title: SampleSms
 * @projectName crud
 * @description: 发送短信
 * @date 2020/7/31下午11:26
 */
public class SampleSms {


    /**
     * 产品名称:云通信短信API产品,开发者无需替换
     */
    static final String PRODUCT = "Dysmsapi";
    /**
     * 产品域名,开发者无需替换
     */
    static final String DOMAIN = "dysmsapi.aliyuncs.com";

    private static SmsProperties smsProperties;

    static {
        smsProperties = SpringUtils.getBean(SmsProperties.class);
    }

    /**
     * 发送短信
     * @param mobile 手机号
     * @param templateCode 模板号
     * @param param 参数
     */
    public static SendSmsResponse sendSms(String mobile, String templateCode, String param) {
        try {
            String accessKeyId = smsProperties.getAccessKeyId();
            String accessKeySecret = smsProperties.getAccessKeySecret();
            String signName = smsProperties.getSignName();
            //可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            //必填:待发送手机号
            request.setPhoneNumbers(mobile);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(signName);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(templateCode);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam(param);
            //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");
            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId("yourOutId");
            return acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            throw new CrudException(ResultStatus.SEND_SMS_ERROR);
        }
    }

    public static QuerySendDetailsResponse querySendDetails(String mobile, String bizId) {
        try {
            String accessKeyId = smsProperties.getAccessKeyId();
            String accessKeySecret = smsProperties.getAccessKeySecret();
            //可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            //组装请求对象
            QuerySendDetailsRequest request = new QuerySendDetailsRequest();
            //必填-号码
            request.setPhoneNumber(mobile);
            //可选-流水号
            request.setBizId(bizId);
            //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
            SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
            request.setSendDate(ft.format(new Date()));
            //必填-页大小
            request.setPageSize(10L);
            //必填-当前页码从1开始计数
            request.setCurrentPage(1L);
            //hint 此处可能会抛出异常，注意catch
            return acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            throw new CrudException(ResultStatus.SEND_SMS_ERROR);
        }
    }
}
