package io.github.yangyouwang.module.system.mapper;

import io.github.yangyouwang.common.base.mapper.BaseMpMapper;
import io.github.yangyouwang.module.system.entity.SysDictValue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据字典值Mapper
 * @author yangyouwang
 */
public interface SysDictValueMapper extends BaseMpMapper<SysDictValue> {
    /**
     * 根据字典类型外键获取字典值
     * @param dictTypeId 字典类型外键
     * @return 字典项列表
     */
    List<SysDictValue> findSysDictValueByDictTypeId(@Param("dictTypeId") Long dictTypeId);
}
