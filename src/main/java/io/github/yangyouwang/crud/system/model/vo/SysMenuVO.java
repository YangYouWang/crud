package io.github.yangyouwang.crud.system.model.vo;


import io.github.yangyouwang.core.util.converter.Treeable;
import io.github.yangyouwang.crud.system.entity.SysMenu;
import lombok.Data;

import java.io.Serializable;
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
public class SysMenuVO implements Treeable, Serializable {

    private static final long serialVersionUID = -8531060973483500513L;
    /**
     * 主键id
     */
    private Long id;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 父菜单ID
     */
    private Long parentId;
    /**
     * 显示顺序
     */
    private Integer orderNum;
    /**
     * 图标
     */
    private String icon;
    /**
     * 请求地址
     */
    private String url;
    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    private String menuType;
    /**
     * 菜单状态（Y显示 N隐藏）
     */
    private String visible;
    /**
     * 权限标识
     */
    private String perms;
    /**
     * 备注
     */
    private String remark;
    /** 子菜单 */
    private List<SysMenu> children = new ArrayList<>();
    /**
     * 父菜单名称
     */
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