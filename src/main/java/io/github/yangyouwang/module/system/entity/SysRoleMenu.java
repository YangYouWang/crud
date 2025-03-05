package io.github.yangyouwang.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 角色菜单中间表
 * sys_role_menu
 * @author yangyouwang
 */
@Data
@TableName("sys_role_menu")
@ApiModel(value="SysRoleMenu对象", description="角色菜单中间表")
public class SysRoleMenu {
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色外键")
    private Long roleId;

    /**
     * 菜单ID
     */
    @ApiModelProperty(value = "菜单外键")
    private Long menuId;

}
