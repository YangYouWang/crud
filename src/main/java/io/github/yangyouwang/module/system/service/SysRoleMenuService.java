package io.github.yangyouwang.module.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.yangyouwang.framework.util.StringUtil;
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
 */
@Service
public class SysRoleMenuService extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> {

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
     * @param roleId 角色id
     * @param menuIdStr 菜单id
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void insertSysRoleMenuBatch(Long roleId, String menuIdStr) {
        Long[] menuIds = StringUtil.getId(menuIdStr);
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
     * 根据菜单ID删除关联关系
     * @param menuId 菜单主键
     */
    public void removeSysRoleMenuByMenuId(Long ... menuId) {
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().in(SysRoleMenu::getMenuId, menuId));
    }
}
