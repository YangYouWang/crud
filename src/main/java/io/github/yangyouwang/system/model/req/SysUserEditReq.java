package io.github.yangyouwang.system.model.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * @author yangyouwang
 * @title: SysUserAddReq
 * @projectName crud
 * @description: 用户编辑
 * @date 2021/3/269:56 AM
 */
@Data
public class SysUserEditReq implements Serializable {

    /**
     * 主键id
     */
    @NotNull(message = "id不能为空")
    private Long id;
    /**
     * 启用
     */
    private String enabled;

    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号码
     */
    private String phonenumber;
    /**
     * 用户性别（0男 1女 2未知）
     */
    private String sex;
    /**
     * 头像
     */
    private String avatar;

    /** 备注 */
    private String remark;

    /**
     * 角色id
     */
    private Long[] roleIds;
}
