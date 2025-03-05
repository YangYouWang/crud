package io.github.yangyouwang.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yangyouwang.common.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户关联角色表
 * sys_user_role
 * @author yangyouwang
 */
@Data
@TableName("sys_user_role")
@ApiModel(value="SysUserRole对象", description="用户关联角色表")
public class SysUserRole extends BaseEntity {
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户外键")
    private Long userId;
    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色外键")
    private Long roleId;
}
