package io.github.yangyouwang.module.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import io.github.yangyouwang.common.base.mapper.BaseMpMapper;
import io.github.yangyouwang.module.system.entity.SysDictType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yangyouwang
 * @title: SysDictTypeMapper
 * @projectName crud
 * @description: 数据字典类型Mapper
 * @date 2021/4/12下午8:07
 */
public interface SysDictTypeMapper extends BaseMpMapper<SysDictType> {
    /**
     * 根据字典别名获取字典
     * @param dictKey 字典key
     * @return 字典
     */
    SysDictType findDictByKey(@Param("dictKey") String dictKey);
    /**
     * 自定义sql分页
     * @param wrapper 参数
     * @return 数据字典列表
     */
    List<SysDictType> selectDictPage(@Param(Constants.WRAPPER) Wrapper wrapper);
}
