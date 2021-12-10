package io.github.yangyouwang.crud.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.yangyouwang.common.constant.Constants;
import io.github.yangyouwang.core.config.SchedulingConfig;
import io.github.yangyouwang.crud.system.mapper.SysTaskMapper;
import io.github.yangyouwang.crud.system.entity.SysTask;
import io.github.yangyouwang.crud.system.model.req.SysTaskAddReq;
import io.github.yangyouwang.crud.system.model.req.SysTaskListReq;
import io.github.yangyouwang.crud.system.model.resp.SysTaskResp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author yangyouwang
 * @title: SysTaskService
 * @projectName crud
 * @description: 任务业务层
 * @date 2021/4/10上午10:26
 */
@Service
public class SysTaskService extends ServiceImpl<SysTaskMapper, SysTask> {

    @Resource
    private SysTaskMapper sysTaskMapper;

    @Resource
    private SchedulingConfig schedulingConfig;
    /**
     * 跳转编辑
     * @return 编辑页面
     */
    @Transactional(readOnly = true)
    public SysTaskResp detail(Long id) {
        SysTask sysTask = this.findTaskById(id);
        SysTaskResp sysTaskResp = new SysTaskResp();
        BeanUtils.copyProperties(sysTask,sysTaskResp);
        return sysTaskResp;
    }

    /**
     * 列表请求
     * @return 请求列表
     */
    @Transactional(readOnly = true)
    public IPage list(SysTaskListReq sysTaskListReq) {
        return sysTaskMapper.selectPage(new Page<>(sysTaskListReq.getPageNum(), sysTaskListReq.getPageSize()),
                new LambdaQueryWrapper<SysTask>()
                        .like(StringUtils.isNotBlank(sysTaskListReq.getName()), SysTask::getName , sysTaskListReq.getName())
                        .like(StringUtils.isNotBlank(sysTaskListReq.getClassName()), SysTask::getClassName , sysTaskListReq.getClassName()));
    }

    /**
     * 添加请求
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    public int add(SysTaskAddReq sysTaskAddReq) {
        SysTask sysTask = new SysTask();
        BeanUtils.copyProperties(sysTaskAddReq,sysTask);
        if (Constants.ENABLED_YES.equals(sysTask.getEnabled())) {
            // 添加任务
            schedulingConfig.addTriggerTask(sysTask.getName(),sysTask.getClassName(),sysTask.getMethodName(),sysTask.getCron());
        }
        return sysTaskMapper.insert(sysTask);
    }

    /**
     * 编辑请求
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    public int edit(SysTask sysTask) {
        // 取消任务
        schedulingConfig.cancelTriggerTask(sysTask.getName());
        if (Constants.ENABLED_YES.equals(sysTask.getEnabled())) {
            // 添加任务
            schedulingConfig.addTriggerTask(sysTask.getName(),sysTask.getClassName(),sysTask.getMethodName(),sysTask.getCron());
            return sysTaskMapper.updateById(sysTask);
        }
        return sysTaskMapper.updateById(sysTask);
    }

    /**
     * 删除请求
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    public int del(Long id) {
        // 查询任务
        SysTask sysTask = this.findTaskById(id);
        // 取消任务
        schedulingConfig.cancelTriggerTask(sysTask.getName());
        // 删除任务
        return sysTaskMapper.deleteById(id);
    }

    /**
     * 查询任务
     * @param id 任务id
     * @return 任务详情
     */
    @Transactional(readOnly = true)
    public SysTask findTaskById(Long id) {
        return sysTaskMapper.selectById(id);
    }
}
