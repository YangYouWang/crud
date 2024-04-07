package io.github.yangyouwang.module.system.service;

import io.github.yangyouwang.module.system.entity.SysLoginLog;
import io.github.yangyouwang.module.system.mapper.SysLoginLogMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
* <p>
 * 用户登录日志记录表 服务实现类
 * </p>
*
* @author yangyouwang
* @since 2022-08-29
*/
@Service
public class SysLoginLogService extends ServiceImpl<SysLoginLogMapper, SysLoginLog> {

  /**
  * 用户登录日志记录表分页列表
  * @param param 参数
  * @return 结果
  */
  public List<SysLoginLog> page(SysLoginLog param) {
    QueryWrapper<SysLoginLog> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda()
      // 账号
          .like(!StringUtils.isEmpty(param.getAccount()), SysLoginLog::getAccount, param.getAccount())
          // 登录IP
          .like(!StringUtils.isEmpty(param.getLoginIp()), SysLoginLog::getLoginIp, param.getLoginIp())
          .orderByDesc(SysLoginLog::getCreateTime);
    return list(queryWrapper);
  }
}
