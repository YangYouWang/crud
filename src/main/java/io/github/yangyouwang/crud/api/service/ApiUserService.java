package io.github.yangyouwang.crud.api.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import io.github.yangyouwang.common.constant.ConfigConsts;
import io.github.yangyouwang.common.enums.ResultStatus;
import io.github.yangyouwang.core.context.ApiContext;
import io.github.yangyouwang.core.exception.CrudException;
import io.github.yangyouwang.core.properties.WeChatProperties;
import io.github.yangyouwang.core.util.DateTimeUtil;
import io.github.yangyouwang.core.util.JwtTokenUtil;
import io.github.yangyouwang.core.util.RestTemplateUtil;
import io.github.yangyouwang.crud.api.model.*;
import io.github.yangyouwang.crud.app.entity.Oauth;
import io.github.yangyouwang.crud.app.entity.SmsCode;
import io.github.yangyouwang.crud.app.entity.User;
import io.github.yangyouwang.crud.app.mapper.UserMapper;
import io.github.yangyouwang.crud.app.service.OauthService;
import io.github.yangyouwang.crud.app.service.SmsCodeService;
import io.github.yangyouwang.crud.system.service.SysDictValueService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * Description: 用户业务层 <br/>
 * date: 2022/8/3 20:46<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
@Service
public class ApiUserService extends ServiceImpl<UserMapper, User> {

    @Resource
    private WeChatProperties weChatProperties;

    @Resource
    private OauthService oauthService;

    @Resource
    private SysDictValueService sysDictValueService;

    @Resource
    private SmsCodeService smsCodeService;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED,rollbackFor = Throwable.class)
    public WxAuthVO wxAuth(WxAuthDTO wxAuthDTO) {
        // 根据微信code获取openId
        String api = ConfigConsts.WEIXIN_OPENID_API.replace("APPID",weChatProperties.getAppID())
                .replace("SECRET",weChatProperties.getAppSecret())
                .replace("JSCODE",wxAuthDTO.getCode());
        String res = RestTemplateUtil.get(api);
        JSONObject jsonObject = JSONObject.parseObject(res);
        if (jsonObject.containsKey("errcode")) {
            throw new CrudException(ResultStatus.WX_LOGIN_ERROR);
        }
        WxAuthVO wxAuthVO = new WxAuthVO();
        String sessionKey = jsonObject.getString("session_key");
        String openId = jsonObject.getString("openid");
        Oauth oauth = oauthService.getOne(new LambdaQueryWrapper<Oauth>().eq(Oauth::getAppSecret,openId).eq(Oauth::getAppType,ConfigConsts.WX_APP_TYPE));
        if (Objects.nonNull(oauth)) {
            // 登录成功
            wxAuthVO.setSessionKey(sessionKey);
            wxAuthVO.setToken(JwtTokenUtil.buildJWT(oauth.getUserId().toString()));
            return wxAuthVO;
        }
        User user = new User();
        user.setAvatar(wxAuthDTO.getAvatarUrl());
        user.setNickName(wxAuthDTO.getNickName());
        user.setGender(wxAuthDTO.getGender());
        user.setStatus(ConfigConsts.USER_STATUS_AVAILABLE);
        this.save(user);
        oauth = new Oauth();
        oauth.setUserId(user.getId());
        oauth.setAppSecret(openId);
        oauth.setAppType(ConfigConsts.WX_APP_TYPE);
        oauthService.save(oauth);
        // 登录成功
        wxAuthVO.setSessionKey(sessionKey);
        wxAuthVO.setToken(JwtTokenUtil.buildJWT(oauth.getUserId().toString()));
        return wxAuthVO;
    }
    /**
     * 根据用户id获取用户详情
     * @return 响应
     */
    @Transactional(readOnly = true)
    public UserInfoVO getUserInfo() {
        Long userId = ApiContext.getUserId();
        User user = getById(userId);
        Assert.notNull(user, "用户不存在");
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user,userInfoVO);
        // 性别
        String sex = sysDictValueService.getDictValueName(ConfigConsts.DICT_KEY_SEX, user.getGender().toString());
        userInfoVO.setGenderLabel(sex);
        return userInfoVO;
    }
    /**
     * 用户名密码授权
     * @return 响应
     */
    public UserAuthVO passwordAuth(PasswordAuthDTO passwordAuthDTO) {
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getMobile, passwordAuthDTO.getMobile()));
        Assert.notNull(user, "用户不存在");
        Oauth oauth = oauthService.getOne(new LambdaQueryWrapper<Oauth>()
                .eq(Oauth::getUserId, user.getId()).eq(Oauth::getAppType,ConfigConsts.PASSWORD_APP_TYPE));
        if (!oauth.getAppSecret().equals(passwordAuthDTO.getAppSecret())) {
            throw new CrudException(ResultStatus.LOGIN_ERROR);
        }
        UserAuthVO userAuthVO = new UserAuthVO();
        userAuthVO.setToken(JwtTokenUtil.buildJWT(oauth.getUserId().toString()));
        return userAuthVO;
    }

    /**
     * 解密微信用户信息
     * @param wxUserInfoDTO 加密微信用户信息
     * @return 手机号
     */
    public String decodeUserInfo(WxUserInfoDTO wxUserInfoDTO) {
        String userPhoneNumber = getUserPhoneNumber(wxUserInfoDTO.getSessionKey(), wxUserInfoDTO.getIv(), wxUserInfoDTO.getEncryptedData());
        Long userId = ApiContext.getUserId();
        User user = new User();
        user.setId(userId);
        user.setMobile(userPhoneNumber);
        this.updateById(user);
        return userPhoneNumber;
    }

    /**
     * 根据微信密钥获取用户手机号
     * @param sessionKey 加密秘钥
     * @param iv 偏移量
     * @param encryptedData 被加密的数据
     * @return 用户手机号
     */
    private String getUserPhoneNumber(String sessionKey,String iv,String encryptedData) {
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                JSONObject jsonObject = JSONObject.parseObject(result);
                return jsonObject.getString("phoneNumber");
            }
        } catch (Exception e) {
            throw new RuntimeException("获取手机号失败");
        }
        return null;
    }
    /**
     * 手机号验证码授权
     * @return 响应
     */
    public UserAuthVO mobileAuth(MobileAuthDTO mobileAuthDTO) {
        String mobile = mobileAuthDTO.getMobile();
        SmsCode smsCode = smsCodeService.getOne(new LambdaQueryWrapper<SmsCode>()
                .eq(SmsCode::getMobile, mobile)
                .eq(SmsCode::getCode, mobileAuthDTO.getCode())
                .eq(SmsCode::getUsable, ConfigConsts.USABLE_EFFECTIVE)
                .eq(SmsCode::getSended, ConfigConsts.SEND_HAS_BEEN_SENT));
        if(Objects.isNull(smsCode)) {
            throw new CrudException(ResultStatus.VERIFICATION_CODE_NOT_EXIST);
        }
        // 当前时间小于过期时间
        boolean flag = DateTimeUtil.compare(new Date(),smsCode.getDeadLine());
        if (!flag) {
            throw new CrudException(ResultStatus.VERIFICATION_CODE_INVALID);
        }
        // 验证码作废
        smsCode.setUsable(ConfigConsts.USABLE_INVALID);
        smsCodeService.updateById(smsCode);
        // 响应
        UserAuthVO userAuthVO = new UserAuthVO();
        // 查询用户
        Oauth oauth = oauthService.getOne(new LambdaQueryWrapper<Oauth>().eq(Oauth::getAppSecret,mobile).eq(Oauth::getAppType,ConfigConsts.PHONE_APP_TYPE));
        if (Objects.nonNull(oauth)) {
            // 登录成功
            userAuthVO.setToken(JwtTokenUtil.buildJWT(oauth.getUserId().toString()));
            return userAuthVO;
        }
        User user = new User();
        user.setMobile(mobile);
        user.setStatus(ConfigConsts.USER_STATUS_AVAILABLE);
        this.save(user);
        oauth = new Oauth();
        oauth.setUserId(user.getId());
        oauth.setAppSecret(mobile);
        oauth.setAppType(ConfigConsts.PHONE_APP_TYPE);
        oauthService.save(oauth);
        // 登录成功
        userAuthVO.setToken(JwtTokenUtil.buildJWT(user.getId().toString()));
        return userAuthVO;
    }
    /**
     * 更新用户信息
     * @param userInfoDTO 用户信息
     * @return 响应
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    public boolean modifyUser(UserInfoDTO userInfoDTO) {
        Long userId = ApiContext.getUserId();
        User user = getById(userId);
        Assert.notNull(user, "用户不存在");
        BeanUtils.copyProperties(userInfoDTO,user);
        return updateById(user);
    }
}
