package io.github.yangyouwang.module.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.yangyouwang.common.domain.XmSelectNode;
import io.github.yangyouwang.framework.util.StringUtil;
import io.github.yangyouwang.module.system.entity.SysUserRole;
import io.github.yangyouwang.module.system.mapper.SysRoleMapper;
import io.github.yangyouwang.module.system.mapper.SysRoleMenuMapper;
import io.github.yangyouwang.module.system.entity.SysRole;
import io.github.yangyouwang.module.system.entity.SysRoleMenu;
import io.github.yangyouwang.module.system.mapper.SysUserRoleMapper;
import org.springframework.aop.framework.AopContext;
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
public class SysRoleService extends ServiceImpl<SysRoleMapper,SysRole> {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

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
        SysRole old = this.getOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleKey,sysRole.getRoleKey()));
        Assert.isNull(old, "角色已存在");
        // 添加角色
        boolean flag = this.save(sysRole);
        if (flag) {
            // 关联菜单
            SysRoleService proxy = (SysRoleService) AopContext.currentProxy();
            proxy.insertRoleMenuBatch(sysRole.getId(), StringUtil.getId(sysRole.getMenuIds()));
            // 关联用户
            proxy.insertRoleUserBatch(sysRole.getId(), StringUtil.getId(sysRole.getUserIds()));
        }
    }

    /**
     * 编辑请求
     * @param sysRole 编辑角色对象
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    public void edit(SysRole sysRole) {
        boolean flag = this.updateById(sysRole);
        if (flag) {
            SysRoleService proxy = (SysRoleService) AopContext.currentProxy();
            // 删除角色菜单
            sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, sysRole.getId()));
            proxy.insertRoleMenuBatch(sysRole.getId(), StringUtil.getId(sysRole.getMenuIds()));
            // 删除角色用户
            sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, sysRole.getId()));
            proxy.insertRoleUserBatch(sysRole.getId(), StringUtil.getId(sysRole.getUserIds()));
        }
    }

    /**
     * 批量新增角色关联菜单
     * @param roleId 角色id
     * @param menuIds 菜单id
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void insertRoleMenuBatch(Long roleId, Long[] menuIds) {
        if (menuIds != null && menuIds.length > 0) {
            List<SysRoleMenu> roleMenus = Arrays.stream(menuIds).map(s -> {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(s);
                return roleMenu;
            }).collect(Collectors.toList());
            sysRoleMenuMapper.insertBatchSomeColumn(roleMenus);
        }
    }

    /**
     * 批量新增角色关联用户
     * @param roleId 角色id
     * @param userIds 用户id
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void insertRoleUserBatch(Long roleId, Long[] userIds) {
        if (userIds != null && userIds.length > 0) {
            List<SysUserRole> userRoles = Arrays.stream(userIds).map(s -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setRoleId(roleId);
                userRole.setUserId(s);
                return userRole;
            }).collect(Collectors.toList());
            sysUserRoleMapper.insertBatchSomeColumn(userRoles);
        }
    }

    /**
     * 角色表删除(单个条目)
     * @param id 主键
     */
    public void remove(Long id) {
        checkRole(Collections.singleton(id));
        removeById(id);
    }

    /**
     * 角色表删除(多个条目)
     * @param ids 主键数组
     */
    public void removes(List<Long> ids) {
        checkRole(ids);
        removeByIds(ids);
    }

    /**
     * 检查角色关联菜单、用户
     * @param ids 角色ids
     */
    @Transactional(readOnly = true)
    public void checkRole(Collection ids) {
        // 查询角色关联菜单
        Integer roleMenuCount = sysRoleMenuMapper.selectCount(new LambdaQueryWrapper<SysRoleMenu>()
                .in(SysRoleMenu::getRoleId, ids));
        if (roleMenuCount > 0) {
            throw new RuntimeException("角色存在菜单，删除失败!");
        }
        // 查询角色关联人员
        Integer userRoleCount = sysUserRoleMapper.selectCount(new LambdaQueryWrapper<SysUserRole>()
                .in(SysUserRole::getRoleId, ids));
        if (userRoleCount > 0) {
            throw new RuntimeException("角色存在用户，删除失败!");
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
