package io.github.yangyouwang.module.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.yangyouwang.framework.util.StringUtil;
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
 */
@Service
public class SysUserPostService extends ServiceImpl<SysUserPostMapper, SysUserPost> {

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
     * @param userId 用户id
     * @param postIdStr 岗位id
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void insertSysUserPostBatchByPostIds(Long userId, String postIdStr) {
        Long[] postIds = StringUtil.getId(postIdStr);
        if (postIds != null && postIds.length > 0) {
            List<SysUserPost> userPosts = Arrays.stream(postIds).map(s -> {
                SysUserPost userPost = new SysUserPost();
                userPost.setUserId(userId);
                userPost.setPostId(s);
                return userPost;
            }).collect(Collectors.toList());
            sysUserPostMapper.insertBatchSomeColumn(userPosts);
        }
    }

}
