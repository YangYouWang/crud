package io.github.yangyouwang.module.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.yangyouwang.common.base.service.BaseService;
import io.github.yangyouwang.framework.util.StringUtil;
import io.github.yangyouwang.module.system.entity.SysRole;
import io.github.yangyouwang.module.system.entity.SysRoleMenu;
import io.github.yangyouwang.module.system.mapper.SysRoleMenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色关联菜单业务层
 * @author yangyouwang
 */
@Service
public class SysRoleMenuService extends BaseService<SysRoleMenuMapper, SysRoleMenu> {

    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    /**
     * 根据角色ID删除关联关系
     * @param roleId 角色主键
     */
    public void removeSysRoleMenuByRoleId(Long ... roleId) {
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().in(SysRoleMenu::getRoleId, roleId));
    }

    /**
     * 批量新增角色关联菜单
     * @param sysRole 角色
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void insertSysRoleMenu(SysRole sysRole) {
        Long[] menuIds = StringUtil.getId(sysRole.getMenuIds());
        if (menuIds != null && menuIds.length > 0) {
            List<SysRoleMenu> roleMenus = Arrays.stream(menuIds).map(s -> {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setRoleId(sysRole.getId());
                roleMenu.setMenuId(s);
                return roleMenu;
            }).collect(Collectors.toList());
            sysRoleMenuMapper.insertBatchSomeColumn(roleMenus);
        }
    }

    /**
     * 根据菜单ID删除关联关系
     * @param menuId 菜单主键
     */
    public void removeSysRoleMenuByMenuId(Long ... menuId) {
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().in(SysRoleMenu::getMenuId, menuId));
    }
}
