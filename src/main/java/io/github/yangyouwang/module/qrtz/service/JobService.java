package io.github.yangyouwang.module.qrtz.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.yangyouwang.common.base.service.BaseService;
import io.github.yangyouwang.common.constant.ConfigConsts;
import io.github.yangyouwang.module.qrtz.util.job.QuartzManager;
import io.github.yangyouwang.module.qrtz.entity.QrtzJob;
import io.github.yangyouwang.module.qrtz.entity.QrtzJobLog;
import io.github.yangyouwang.module.qrtz.mapper.JobLogMapper;
import io.github.yangyouwang.module.qrtz.mapper.JobMapper;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 任务表 服务实现类
 * </p>
 *
 * @author yangyouwang
 * @since 2022-07-30
 */
@Service
public class JobService extends BaseService<JobMapper, QrtzJob> {

  @Resource
  private QuartzManager quartzManager;

  @Resource
  private JobLogMapper jobLogMapper;

  /**
   * 项目启动时，初始化定时器
   * 主要是防止手动修改数据库导致未同步到定时任务处理（注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
   */
  @PostConstruct
  public void init() throws SchedulerException
  {
    for (QrtzJob task : list())
    {
      quartzManager.addJob(task);
    }
  }
  /**
   * 任务表分页列表
   * @param param 分页DTO
   * @return 结果
   */
  public List<QrtzJob> page(QrtzJob param) {
    LambdaQueryWrapper<QrtzJob> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.like(StringUtils.isNotBlank(param.getJobName()), QrtzJob::getJobName,param.getJobName())
            .like(StringUtils.isNotBlank(param.getJobGroup()), QrtzJob::getJobGroup,param.getJobGroup())
            .orderByDesc(QrtzJob::getCreateTime);
    return list(queryWrapper);
  }

  /**
   * 任务表新增
   * @param param 根据需要进行传值
   */
  public void add(QrtzJob param) {
    quartzManager.addJob(param);
    save(param);
  }

  /**
   * 任务表修改
   * @param param 根据需要进行传值
   */
  public void modify(QrtzJob param) {
    try {
      quartzManager.updateJobCron(param);
    } catch (SchedulerException e) {
      throw new RuntimeException("修改任务异常");
    }
    updateById(param);
  }

  /**
   * 任务表删除(单个条目)
   * @param id 主键
   */
  public void remove(Long id) {
    QrtzJob job = getById(id);
    try {
      quartzManager.deleteJob(job);
    } catch (SchedulerException e) {
      throw new RuntimeException("删除任务异常");
    }
    // 删除调度
    removeById(id);
    // 删除调度日志
    jobLogMapper.delete(new LambdaQueryWrapper<QrtzJobLog>()
            .eq(QrtzJobLog::getJobId,id));
  }

  /**
   * 任务表删除(多个条目)
   * @param ids 主键数组
   */
  public void removes(List<Long> ids) {
    removeByIds(ids);
  }

  /**
   * 修改任务状态
   * @param param 任务对象
   * @return 修改任务对象响应
   */
  public boolean changeJob(QrtzJob param) {
    QrtzJob job = getById(param.getId());
    try {
      if (ConfigConsts.SYS_YES.equals(param.getEnabled())) {
        // 恢复某个定时任务
        quartzManager.resumeJob(job);
      } else {
        // 暂停某个定时任务（任务恢复后，暂停时间段内未执行的任务会继续执行，如暂停时间段内有2次，则会执行2次）
        quartzManager.pauseJob(job);
      }
    } catch (SchedulerException e) {
      throw new RuntimeException("修改定时任务异常");
    }
    return updateById(param);
  }
}
