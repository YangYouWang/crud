package io.github.yangyouwang.module.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.yangyouwang.common.base.service.BaseService;
import io.github.yangyouwang.framework.util.StringUtil;
import io.github.yangyouwang.module.system.entity.SysRole;
import io.github.yangyouwang.module.system.entity.SysUser;
import io.github.yangyouwang.module.system.entity.SysUserRole;
import io.github.yangyouwang.module.system.mapper.SysUserRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户关联角色业务层
 * @author yangyouwang
 */
@Service
public class SysUserRoleService extends BaseService<SysUserRoleMapper, SysUserRole> {


    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * 删除用户ID删除关联关系
     */
    public void removeSysUserRoleByRoleId(Long ... roleId) {
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getRoleId, roleId));
    }

    /**
     * 批量新增用户关联角色
     * @param sysRole 角色
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void insertSysUserRole(SysRole sysRole) {
        Long[] userIds = StringUtil.getId(sysRole.getUserIds());
        if (userIds != null && userIds.length > 0) {
            List<SysUserRole> userRoles = Arrays.stream(userIds).map(s -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setRoleId(sysRole.getId());
                userRole.setUserId(s);
                return userRole;
            }).collect(Collectors.toList());
            sysUserRoleMapper.insertBatchSomeColumn(userRoles);
        }
    }

    /**
     * 根据用户ID删除关联关系
     */
    public void removeSysUserRoleByUserId(Long ... userId) {
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getUserId, userId));
    }

    /**
     * 批量新增修改用户关联角色
     * @param sysUser 用户
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void insertSysUserRole(SysUser sysUser) {
        Long[] roleIds = StringUtil.getId(sysUser.getRoleIds());
        if (roleIds != null && roleIds.length > 0) {
            List<SysUserRole> userRoles = Arrays.stream(roleIds).map(s -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(sysUser.getId());
                userRole.setRoleId(s);
                return userRole;
            }).collect(Collectors.toList());
            sysUserRoleMapper.insertBatchSomeColumn(userRoles);
        }
    }
}
