package io.github.yangyouwang.module.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.yangyouwang.common.base.domain.XmSelectNode;
import io.github.yangyouwang.common.base.service.BaseService;
import io.github.yangyouwang.framework.util.StringUtil;
import io.github.yangyouwang.module.system.mapper.SysRoleMapper;
import io.github.yangyouwang.module.system.entity.SysRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.thymeleaf.util.ArrayUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.*;

/**
 * @author yangyouwang
 * @title: SysRoleService
 * @projectName crud
 * @description: 角色业务层
 * @date 2021/3/269:44 PM
 */
@Service
public class SysRoleService extends BaseService<SysRoleMapper,SysRole> {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Resource
    private SysUserRoleService sysUserRoleService;

    /**
     * 跳转编辑
     * @param id 角色id
     * @return 编辑页面
     */
    @Transactional(readOnly = true)
    public SysRole detail(Long id) {
        return sysRoleMapper.findRoleById(id);
    }

    /**
     * 添加请求
     * @param sysRole 添加角色对象
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    public void add(@NotNull SysRole sysRole) {
        SysRole sysRoleOld = this.getOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleKey,sysRole.getRoleKey()));
        Assert.isNull(sysRoleOld, "角色已存在");
        if (this.save(sysRole)) {
            sysRoleMenuService.insertSysRoleMenu(sysRole);
            sysUserRoleService.insertSysUserRole(sysRole);
        }
    }

    /**
     * 编辑请求
     * @param sysRole 编辑角色对象
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    public void edit(SysRole sysRole) {
        if (this.updateById(sysRole)) {
            sysRoleMenuService.removeSysRoleMenuByRoleId(sysRole.getId());
            sysUserRoleService.removeSysUserRoleByRoleId(sysRole.getId());
            sysRoleMenuService.insertSysRoleMenu(sysRole);
            sysUserRoleService.insertSysUserRole(sysRole);
        }
    }

    /**
     * 角色表删除(单个条目)
     * @param id 主键
     */
    public void remove(Long id) {
        if (this.removeById(id)) {
            sysRoleMenuService.removeSysRoleMenuByRoleId(id);
            sysUserRoleService.removeSysUserRoleByRoleId(id);
        }
    }

    /**
     * 角色表删除(多个条目)
     * @param ids 主键数组
     */
    public void removes(List<Long> ids) {
        if (this.removeByIds(ids)) {
            sysRoleMenuService.removeSysRoleMenuByRoleId(ids.toArray(new Long[0]));
            sysUserRoleService.removeSysUserRoleByRoleId(ids.toArray(new Long[0]));
        }
    }

    /**
     * 根据角色ids查询选中角色列表
     * @param ids 角色ids
     * @return 角色列表
     */
    @Transactional(readOnly = true)
    public List<XmSelectNode> xmSelect(String ids) {
        List<SysRole> sysRoles = this.list(new LambdaQueryWrapper<>());
        return sysRoles.stream().map(sysRole -> {
            XmSelectNode treeNode = new XmSelectNode();
            treeNode.setName(sysRole.getRoleName());
            treeNode.setValue(sysRole.getId());
            treeNode.setId(sysRole.getId());
            ofNullable(ids).ifPresent(optIds -> treeNode.setSelected(ArrayUtils.contains(Objects.requireNonNull(StringUtil.getId(optIds)),sysRole.getId())));
            return treeNode;
        }).collect(Collectors.toList());
    }
}
