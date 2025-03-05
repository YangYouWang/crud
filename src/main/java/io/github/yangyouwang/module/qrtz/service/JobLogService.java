package io.github.yangyouwang.module.qrtz.service;

import io.github.yangyouwang.common.base.service.BaseService;
import io.github.yangyouwang.module.qrtz.entity.QrtzJobLog;
import io.github.yangyouwang.module.qrtz.mapper.JobLogMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
* <p>
 * 任务日志 服务实现类
 * </p>
*
* @author yangyouwang
* @since 2022-10-26
*/
@Service
public class JobLogService extends BaseService<JobLogMapper, QrtzJobLog> {

  /**
  * 任务日志分页列表
  * @param param 参数
  * @return 结果
  */
  public List<QrtzJobLog> page(QrtzJobLog param) {
    QueryWrapper<QrtzJobLog> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda()
          // 任务名称
          .like(!StringUtils.isEmpty(param.getJobName()), QrtzJobLog::getJobName, param.getJobName())
          // 任务组名
          .like(!StringUtils.isEmpty(param.getJobGroup()), QrtzJobLog::getJobGroup, param.getJobGroup())
          // 任务外键
          .like(null != param.getJobId(), QrtzJobLog::getJobId, param.getJobId())
    .orderByDesc(QrtzJobLog::getCreateTime);
    return list(queryWrapper);
  }

  /**
  * 任务日志详情
  * @param id 主键
  * @return 结果
  */
  public QrtzJobLog info(Long id) {
    return getById(id);
  }

  /**
  * 任务日志新增
  * @param param 根据需要进行传值
  */
  public void add(QrtzJobLog param) {
    save(param);
  }

  /**
  * 任务日志修改
  * @param param 根据需要进行传值
  */
  public void modify(QrtzJobLog param) {
    updateById(param);
  }

  /**
  * 任务日志删除(单个条目)
  * @param id 主键
  */
  public void remove(Long id) {
    removeById(id);
  }

  /**
  * 任务日志删除(多个条目)
  * @param ids 主键数组
  */
  public void removes(List<Long> ids) {
     removeByIds(ids);
   }
}
