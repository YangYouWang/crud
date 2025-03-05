package io.github.yangyouwang.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yangyouwang.common.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * <p>
 * 用户登录日志记录表
 * </p>
 *
 * @author yangyouwang
 * @since 2022-08-29
 */
@Data
@TableName("sys_login_log")
@ApiModel(value="SysLoginLog对象", description="用户登录日志记录表")
public class SysLoginLog extends BaseEntity {

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "登录IP")
    private String loginIp;
}
