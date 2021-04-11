package io.github.yangyouwang.crud.system.model.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * @author yangyouwang
 * @title: SysMenuEditReq
 * @projectName crud
 * @description: 菜单编辑
 * @date 2021/3/269:56 AM
 */
@Data
public class SysMenuEditReq implements Serializable {

    /**
     * 主键id
     */
    @NotNull(message = "id不能为空")
    private Long id;
    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    private String menuName;
    /**
     * 父菜单ID
     */
    @NotNull(message = "父菜单ID不能为空")
    private Long parentId;
    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    private Integer orderNum;
    /**
     * 请求地址
     */
    private String url;
    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    @NotBlank(message = "菜单类型不能为空")
    private String menuType;
    /**
     * 菜单状态（Y显示 N隐藏）
     */
    @NotBlank(message = "菜单状态不能为空")
    private String visible;
    /**
     * 权限标识
     */
    private String perms;

    /** 备注 */
    private String remark;
}
