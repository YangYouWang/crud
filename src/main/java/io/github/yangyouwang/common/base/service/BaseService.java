package io.github.yangyouwang.common.base.service;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 基础业务层
 */
public abstract class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {
}
