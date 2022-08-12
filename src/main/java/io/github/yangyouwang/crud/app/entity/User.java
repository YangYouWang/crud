package io.github.yangyouwang.crud.app.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author yangyouwang
 * @since 2022-08-03
 */
@Data
@TableName("app_user")
@ApiModel(value="User对象", description="用户表")
public class User {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户昵称或网络名称")
    private String nickName;

    @ApiModelProperty(value = "用户头像图片")
    private String avatar;

    @ApiModelProperty(value = "性别：1时是男性，值为2时是女性，值为0时是未知")
    private Integer gender;

    @ApiModelProperty(value = "生日")
    private Date birthday;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "0 可用, 1 禁用, 2 注销")
    private Integer status;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;
}