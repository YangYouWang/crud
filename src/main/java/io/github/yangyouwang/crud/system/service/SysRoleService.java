package io.github.yangyouwang.crud.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.yangyouwang.common.domain.XmSelectNode;
import io.github.yangyouwang.common.enums.ResultStatus;
import io.github.yangyouwang.core.exception.CrudException;
import io.github.yangyouwang.crud.system.mapper.SysRoleMapper;
import io.github.yangyouwang.crud.system.mapper.SysRoleMenuMapper;
import io.github.yangyouwang.crud.system.entity.SysMenu;
import io.github.yangyouwang.crud.system.entity.SysRole;
import io.github.yangyouwang.crud.system.entity.SysRoleMenu;
import io.github.yangyouwang.crud.system.model.req.SysRoleAddReq;
import io.github.yangyouwang.crud.system.model.req.SysRoleEditReq;
import io.github.yangyouwang.crud.system.model.req.SysRoleListReq;
import io.github.yangyouwang.crud.system.model.resp.SysRoleResp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.thymeleaf.util.ArrayUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
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

    /**
     * 跳转编辑
     * @param id 角色id
     * @return 编辑页面
     */
    @Transactional(readOnly = true)
    public SysRoleResp detail(Long id) {
        SysRole sysRole = sysRoleMapper.findRoleById(id);
        SysRoleResp sysRoleResp = new SysRoleResp();
        BeanUtils.copyProperties(sysRole,sysRoleResp);
        ofNullable(sysRole.getMenus()).ifPresent(menus -> {
            Long[] menuIds = menus.stream().map(SysMenu::getId).toArray(Long[]::new);
            sysRoleResp.setMenuIds(menuIds);
        });
        return sysRoleResp;
    }

    /**
     * 列表请求
     * @param sysRoleListReq 请求角色列表对象
     * @return 请求列表
     */
    @Transactional(readOnly = true)
    public IPage list(SysRoleListReq sysRoleListReq) {
        return this.page(new Page<>(sysRoleListReq.getPageNum(), sysRoleListReq.getPageSize()),
                new LambdaQueryWrapper<SysRole>()
                        .like(StringUtils.isNotBlank(sysRoleListReq.getRoleName()), SysRole::getRoleName , sysRoleListReq.getRoleName())
                        .like(StringUtils.isNotBlank(sysRoleListReq.getRoleKey()), SysRole::getRoleKey , sysRoleListReq.getRoleKey()));
    }

    /**
     * 添加请求
     * @param sysRoleAddReq 添加角色对象
     * @return 添加角色状态
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    public int add(@NotNull SysRoleAddReq sysRoleAddReq) {
        SysRole sysRole = sysRoleMapper.selectOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleKey,sysRoleAddReq.getRoleKey()));
        Assert.isNull(sysRole, ResultStatus.ROLE_EXIST_ERROR.message);
        // 添加角色
        SysRole role = new SysRole();
        BeanUtils.copyProperties(sysRoleAddReq,role);
        int flag = sysRoleMapper.insert(role);
        if (flag > 0) {
            // 关联菜单
            SysRoleService proxy = (SysRoleService) AopContext.currentProxy();
            proxy.insertRoleMenuBatch(role.getId(), sysRoleAddReq.getMenuIds());
            return flag;
        }
        throw new CrudException(ResultStatus.SAVE_DATA_ERROR);
    }

    /**
     * 编辑请求
     * @param sysRoleEditReq 编辑角色对象
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    public int edit(SysRoleEditReq sysRoleEditReq) {
        SysRole role = new SysRole();
        BeanUtils.copyProperties(sysRoleEditReq,role);
        int flag = sysRoleMapper.updateById(role);
        // 关联菜单
        if (flag > 0) {
            if (sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>()
                    .eq(SysRoleMenu::getRoleId, role.getId())) > 0) {
                SysRoleService proxy = (SysRoleService) AopContext.currentProxy();
                proxy.insertRoleMenuBatch(role.getId(), sysRoleEditReq.getMenuIds());
                return flag;
            }
        }
        throw new CrudException(ResultStatus.UPDATE_DATA_ERROR);
    }

    /**
     * 批量新增角色关联菜单
     * @param roleId 角色id
     * @param menuIds 菜单id
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void insertRoleMenuBatch(Long roleId, Long[] menuIds) {
        List<SysRoleMenu> roleMenus = Arrays.stream(menuIds).map(s -> {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(s);
            return roleMenu;
        }).collect(Collectors.toList());
        int flag = sysRoleMenuMapper.insertBatchSomeColumn(roleMenus);
        if (flag == 0) {
            throw new CrudException(ResultStatus.BATCH_INSTALL_ROLE_ERROR);
        }
    }

    /**
     * 删除请求
     * @param id 角色id
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    public void del(Long id) {
        sysRoleMapper.deleteById(id);
        // 删除角色关联菜单
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>()
                .eq(SysRoleMenu::getRoleId,id));
    }

    /**
     * 查询角色列表
     * @param ids 角色ids
     * @return 角色列表
     */
    @Transactional(readOnly = true)
    public List<XmSelectNode> xmSelect(Long[] ids) {
        List<SysRole> sysRoles = sysRoleMapper.selectList(new LambdaQueryWrapper<>());
        return sysRoles.stream().map(sysRole -> {
            XmSelectNode treeNode = new XmSelectNode();
            treeNode.setName(sysRole.getRoleName());
            treeNode.setValue(sysRole.getId());
            treeNode.setId(sysRole.getId());
            ofNullable(ids).ifPresent(optIds -> treeNode.setSelected(ArrayUtils.contains(optIds,sysRole.getId())));
            return treeNode;
        }).collect(Collectors.toList());
    }
}
