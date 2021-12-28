package io.github.yangyouwang.crud.system.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.yangyouwang.common.constant.Constants;
import io.github.yangyouwang.common.enums.ResultStatus;
import io.github.yangyouwang.core.exception.CrudException;
import io.github.yangyouwang.crud.system.entity.SysMenu;
import io.github.yangyouwang.crud.system.entity.SysRole;
import io.github.yangyouwang.crud.system.entity.SysUser;
import io.github.yangyouwang.crud.system.entity.SysUserRole;
import io.github.yangyouwang.crud.system.mapper.SysMenuMapper;
import io.github.yangyouwang.crud.system.mapper.SysUserMapper;
import io.github.yangyouwang.crud.system.mapper.SysUserRoleMapper;
import io.github.yangyouwang.crud.system.model.req.*;
import io.github.yangyouwang.crud.system.model.resp.SysUserResp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.*;

/**
 * @author yangyouwang
 * @title: SysUserService
 * @projectName crud
 * @description: 用户业务层
 * @date 2021/3/2112:37 AM
 */
@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> implements UserDetailsService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(@NonNull String userName) {
        // 通过用户名从数据库获取用户信息
        SysUser user  = sysUserMapper.findUserByName(userName);
        if (ObjectUtil.isNull(user)) {
            throw new UsernameNotFoundException(ResultStatus.LOGIN_ERROR.message);
        }
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        for (SysRole role : user.getRoles()) {
            List<SysMenu> menus = sysMenuMapper.findMenuByRoleId(role.getId());
            for (SysMenu menu : menus) {
                authorities.add(new SimpleGrantedAuthority(menu.getPerms()));
            }
            authorities.add(new SimpleGrantedAuthority(Constants.ROLE_PREFIX + role.getRoleKey()));
        }
        return new User(user.getUserName(), user.getPassWord(), Constants.ENABLED_YES.equals(user.getEnabled()),
                true, true, true, authorities);
    }

    /**
     * 用户详情
     * @param id 用户id
     * @return 用户详情
     */
    @Transactional(readOnly = true)
    public SysUserResp detail(Long id) {
        SysUser sysUser = sysUserMapper.findUserByUserId(id);
        SysUserResp sysUserResp = new SysUserResp();
        BeanUtils.copyProperties(sysUser,sysUserResp);
        ofNullable(sysUser.getRoles()).ifPresent(sysRoles -> {
            Long[] roleIds = sysRoles.stream().map(SysRole::getId).toArray(Long[]::new);
            sysUserResp.setRoleIds(roleIds);
        });
        return sysUserResp;
    }

    /**
     * 列表请求
     * @param sysUserListReq 用户列表对象
     * @return 列表
     */
    @Transactional(readOnly = true)
    public IPage list(SysUserListReq sysUserListReq) {
        return this.page(new Page<>(sysUserListReq.getPageNum(), sysUserListReq.getPageSize()),
                new LambdaQueryWrapper<SysUser>()
                        .like(StringUtils.isNotBlank(sysUserListReq.getUserName()), SysUser::getUserName , sysUserListReq.getUserName())
                        .like(StringUtils.isNotBlank(sysUserListReq.getEmail()), SysUser::getEmail , sysUserListReq.getEmail())
                        .like(StringUtils.isNotBlank(sysUserListReq.getPhonenumber()), SysUser::getPhonenumber , sysUserListReq.getPhonenumber()));
    }

    /**
     * 添加请求
     * @param sysUserAddReq 添加用户对象
     * @return 添加状态
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    public int add(SysUserAddReq sysUserAddReq) {
        SysUser sysUser = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName,sysUserAddReq.getUserName()));
        Assert.isNull(sysUser,ResultStatus.USER_EXIST_ERROR.message);
        SysUser user = new SysUser();
        BeanUtils.copyProperties(sysUserAddReq,user);
        String passWord = passwordEncoder.encode(sysUserAddReq.getPassWord());
        user.setPassWord(passWord);
        int flag = sysUserMapper.insert(user);
        if (flag > 0) {
            SysUserService proxy = (SysUserService) AopContext.currentProxy();
            proxy.insertUserRoleBatch(user.getId(), sysUserAddReq.getRoleIds());
            return flag;
        }
        throw new CrudException(ResultStatus.SAVE_DATA_ERROR);
    }

    /**
     * 编辑请求
     * @param sysUserEditReq 编辑用户对象
     * @return 编辑状态
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    public int edit(SysUserEditReq sysUserEditReq) {
        SysUser user = new SysUser();
        BeanUtil.copyProperties(sysUserEditReq,user,true, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        int flag = sysUserMapper.updateById(user);
        if (flag > 0) {
            if (sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                    .eq(SysUserRole::getUserId, user.getId())) > 0) {
                SysUserService proxy = (SysUserService) AopContext.currentProxy();
                proxy.insertUserRoleBatch(user.getId(), sysUserEditReq.getRoleIds());
            }
            return flag;
        }
        throw new CrudException(ResultStatus.UPDATE_DATA_ERROR);
    }

    /**
     * 编辑请求
     * @param sysUserEditUserInfoReq 编辑用户对象
     * @return 编辑状态
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    public int editUserInfo(SysUserEditUserInfoReq sysUserEditUserInfoReq) {
        SysUser user = new SysUser();
        BeanUtil.copyProperties(sysUserEditUserInfoReq,user,true, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        return sysUserMapper.updateById(user);
    }

    /**
     * 批量新增修改用户关联角色
     * @param userId 用户id
     * @param roleIds 角色id
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void insertUserRoleBatch(Long userId, Long[] roleIds) {
        List<SysUserRole> userRoles = Arrays.stream(roleIds).map(s -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(s);
            return userRole;
        }).collect(Collectors.toList());
        int flag = sysUserRoleMapper.insertBatchSomeColumn(userRoles);
        if (flag == 0) {
            throw new CrudException(ResultStatus.BATCH_INSTALL_USER_ERROR);
        }
    }

    /**
     * 删除请求
     * @param id 用户id
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    public void del(Long id) {
        sysUserMapper.deleteById(id);
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId,id));
    }

    /**
     * 修改用户状态
     * @param sysUserEnabledReq 用户状态dto
     * @return 修改状态
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    public int changeUser(SysUserEnabledReq sysUserEnabledReq) {
        SysUser sysUser = new SysUser();
        sysUser.setId(sysUserEnabledReq.getId());
        sysUser.setEnabled(sysUserEnabledReq.getEnabled());
        return sysUserMapper.updateById(sysUser);
    }

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    @Transactional(readOnly = true)
    public SysUserResp findUserByName(String username) {
        SysUser sysUser = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, username));
        SysUserResp sysUserResp = new SysUserResp();
        BeanUtils.copyProperties(sysUser,sysUserResp);
        return sysUserResp;
    }

    /**
     * 导出用户列表
     * @return 用户列表
     */
    @Transactional(readOnly = true)
    public List<SysUserResp> exportSysUserList() {
        List<SysUser> sysUsers = sysUserMapper.selectList(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEnabled, Constants.ENABLED_YES));
        return sysUsers.stream().map(sysUser -> {
            SysUserResp sysUserResp = new SysUserResp();
            BeanUtils.copyProperties(sysUser, sysUserResp);
            return sysUserResp;
        }).collect(Collectors.toList());
    }

    /**
     * 修改密码
     * @param modifyPassReq 修改密码对象
     * @return 修改状态
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    public int modifyPass(ModifyPassReq modifyPassReq) {
        SysUser sysUser = sysUserMapper.selectById(modifyPassReq.getId());
        boolean matches = passwordEncoder.matches(modifyPassReq.getOldPassword(),sysUser.getPassWord());
        Assert.isTrue(matches,ResultStatus.OLD_PASSWORD_ERROR.message);
        String password = passwordEncoder.encode(modifyPassReq.getPassword());
        sysUser.setPassWord(password);
        return sysUserMapper.updateById(sysUser);
    }

    /**
     * 重置密码
     * @param sysUserResetPassReq 重置用户密码对象
     * @return 重置密码
     */
    public int resetPass(SysUserResetPassReq sysUserResetPassReq) {
        SysUser sysUser = new SysUser();
        String password = passwordEncoder.encode(sysUserResetPassReq.getPassWord());
        sysUser.setId(sysUserResetPassReq.getId());
        sysUser.setPassWord(password);
        return sysUserMapper.updateById(sysUser);
    }
}