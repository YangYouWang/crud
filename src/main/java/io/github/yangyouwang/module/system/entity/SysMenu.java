package io.github.yangyouwang.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yangyouwang.common.base.entity.BaseTreeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yangyouwang
 * @title: SysMenu
 * @projectName crud
 * @description: 菜单类
 * @date 2021/3/2112:22 AM
 */
@Data
@TableName("sys_menu")
@ApiModel(value="SysMenu对象", description="菜单表")
public class SysMenu extends BaseTreeEntity {
    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String menuName;
    /**
     * 显示顺序
     */
    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum;
    /**
     * 图标
     */
    @ApiModelProperty(value = "图标")
    private String icon;
    /**
     * 请求地址
     */
    @ApiModelProperty(value = "请求地址")
    private String url;
    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    @ApiModelProperty(value = "菜单类型（M目录 C菜单 F按钮）")
    private String menuType;
    /**
     * 菜单状态（Y显示 N隐藏）
     */
    @ApiModelProperty(value = "菜单状态（Y显示 N隐藏）")
    private String visible;
    /**
     * 权限标识
     */
    @ApiModelProperty(value = "权限标识")
    private String perms;
}
