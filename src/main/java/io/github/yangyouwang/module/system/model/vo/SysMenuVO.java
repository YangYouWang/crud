package io.github.yangyouwang.module.system.model.vo;


import io.github.yangyouwang.framework.util.converter.Treeable;
import io.github.yangyouwang.module.system.entity.SysMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yangyouwang
 * @title: SysMenuDTO
 * @projectName crud
 * @description: 菜单响应
 * @date 2021/3/254:43 PM
 */
@Data
@ApiModel("菜单响应")
public class SysMenuVO implements Treeable {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键")
    private Long id;
    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String menuName;
    /**
     * 父菜单ID
     */
    @ApiModelProperty(value = "父菜单外键")
    private Long parentId;
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
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /** 子菜单 */
    @ApiModelProperty(value = "子菜单")
    private List<SysMenu> children = new ArrayList<>();
    /**
     * 父菜单名称
     */
    @ApiModelProperty(value = "父菜单名称")
    private String parentName;

    @Override
    public Long getMapKey() {
        return parentId;
    }

    @Override
    public Long getChildrenKey() {
        return id;
    }

    @Override
    public Long getRootKey() {
        return 0L;
    }

    @Override
    public void setChildren(List children) {
        this.children = children;
    }
}
