package io.github.yangyouwang.module.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.yangyouwang.common.base.service.BaseService;
import io.github.yangyouwang.framework.util.StringUtil;
import io.github.yangyouwang.module.system.entity.SysUser;
import io.github.yangyouwang.module.system.entity.SysUserPost;
import io.github.yangyouwang.module.system.mapper.SysUserPostMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户关联岗位业务层
 * @author yangyouwang
 */
@Service
public class SysUserPostService extends BaseService<SysUserPostMapper, SysUserPost> {

    @Resource
    private SysUserPostMapper sysUserPostMapper;

    /**
     * 根据用户ID删除关联关系
     */
    public void removeSysUserPostByUserId(Long ... userId) {
        sysUserPostMapper.delete(new LambdaQueryWrapper<SysUserPost>().in(SysUserPost::getUserId,  userId));
    }

    /**
     * 批量新增修改用户关联岗位
     * @param sysUser 用户
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void insertSysUserPost(SysUser sysUser) {
        Long[] postIds = StringUtil.getId(sysUser.getPostIds());
        if (postIds != null && postIds.length > 0) {
            List<SysUserPost> userPosts = Arrays.stream(postIds).map(s -> {
                SysUserPost userPost = new SysUserPost();
                userPost.setUserId(sysUser.getId());
                userPost.setPostId(s);
                return userPost;
            }).collect(Collectors.toList());
            sysUserPostMapper.insertBatchSomeColumn(userPosts);
        }
    }

}
