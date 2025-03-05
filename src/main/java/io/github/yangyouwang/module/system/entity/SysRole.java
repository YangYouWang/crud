package io.github.yangyouwang.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yangyouwang.common.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yangyouwang
 * @title: SysRole
 * @projectName crud
 * @description: 角色类
 * @date 2021/3/2112:22 AM
 */
@Data
@TableName("sys_role")
@ApiModel(value="SysRole对象", description="角色表")
public class SysRole extends BaseEntity {
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    /**
     * 角色标识
     */
    @ApiModelProperty(value = "角色标识")
    private String roleKey;
    /**
     * 菜单ID数组
     */
    @ApiModelProperty(value = "菜单外键数组")
    @TableField(exist = false)
    private String menuIds;
    /**
     * 用户ID数组
     */
    @ApiModelProperty(value = "用户外键数组")
    @TableField(exist = false)
    private String userIds;
}
