package io.github.yangyouwang.common.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;

/**
 * @author yangyouwang
 * @title: CrudBaseMapper
 * @projectName crud
 * @description: 扩展Mp通用Mapper
 * @date 2021/04/30 上午9:30
 */
public interface BaseMpMapper<T> extends BaseMapper<T> {

    /**
     * 批量插入 仅适用于mysql
     *
     * @param entityList 实体列表
     */
    void insertBatchSomeColumn(Collection<T> entityList);
}
