package io.github.yangyouwang.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yangyouwang.common.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yangyouwang
 * @title: SysUser
 * @projectName crud
 * @description: 用户表
 * @date 2021/3/2112:22 AM
 */
@Data
@TableName("sys_user")
@ApiModel(value="SysUser对象", description="用户表")
public class SysUser extends BaseEntity {
    /**
     * 部门外键
     */
    @ApiModelProperty(value = "部门外键")
    private Long deptId;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;
    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String userName;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String passWord;
    /**
     * 启用
     */
    @ApiModelProperty(value = "启用")
    private String enabled;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;
    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String phonenumber;
    /**
     * 用户性别（1男 2女 0未知）
     */
    @ApiModelProperty(value = "用户性别（1男 2女 0未知）")
    private String sex;
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String avatar;
    /**
     * 角色ID数组
     */
    @ApiModelProperty(value = "角色ID数组")
    @TableField(exist = false)
    private String roleIds;
    /**
     * 岗位ID数组
     */
    @ApiModelProperty(value = "岗位ID数组")
    @TableField(exist = false)
    private String postIds;
    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    @TableField(exist = false)
    private String deptName;
    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    @TableField(exist = false)
    private String postName;
}
