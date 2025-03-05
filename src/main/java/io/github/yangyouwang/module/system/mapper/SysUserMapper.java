package io.github.yangyouwang.module.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import io.github.yangyouwang.common.base.mapper.BaseMpMapper;
import io.github.yangyouwang.module.system.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author yangyouwang
 * @title: SysUserMapper
 * @projectName crud
 * @description: 用户Mapper
 * @date 2021/3/2112:25 AM
 */
public interface SysUserMapper extends BaseMpMapper<SysUser> {

    /**
     * 根据用户名称查询用户信息
     * @param userName 用户名称
     * @return 用户信息
     */
    SysUser findUserByName(String userName);

    /**
     * 根据用户id查询用户详情
     * @param id 用户id
     * @return 用户详情
     */
    SysUser findUserByUserId(Long id);

    /**
     * 获取全部用户
     * @param wrapper 参数
     * @return 用户列表
     */
    List<SysUser> findUserList(@Param(Constants.WRAPPER) Wrapper wrapper);
}
