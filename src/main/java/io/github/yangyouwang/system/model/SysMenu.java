package io.github.yangyouwang.system.model;

import io.github.yangyouwang.common.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * @author yangyouwang
 * @title: SysMenu
 * @projectName crud
 * @description: 菜单类
 * @date 2021/3/2112:22 AM
 */
@Data
@Entity
@Table(name="sys_menu")
@AllArgsConstructor
@NoArgsConstructor
public class SysMenu extends BaseEntity {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    /**
     * 菜单名称
     */
    @Column(name="menu_name")
    private String menuName;
    /**
     * 父菜单ID
     */
    @Column(name="parent_id")
    private Long parentId;
    /**
     * 显示顺序
     */
    @Column(name="order_num")
    private Integer orderNum;
    /**
     * 请求地址
     */
    @Column(name="url")
    private String url;
    /**
     * 打开方式（menuItem页签 menuBlank新窗口）
     */
    @Column(name="target")
    private String target;
    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    @Column(name="menu_type")
    private String menuType;
    /**
     * 菜单状态（Y显示 N隐藏）
     */
    @Column(name="visible")
    private String visible;
    /**
     * 权限标识
     */
    @Column(name="perms")
    private String perms;
    /**
     * 菜单图标
     */
    @Column(name="icon")
    private String icon;

    /**
     * 一个菜单对应多个角色
     */
    @ManyToMany(targetEntity = SysUser.class)
    @JoinTable(name="sys_role_menu",
            joinColumns={@JoinColumn(name="menu_id",referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="role_id",referencedColumnName="id")}
    )
    private List<SysRole> roles;

    /** 子菜单 */
    @Transient
    private List<SysMenu> children;
}
