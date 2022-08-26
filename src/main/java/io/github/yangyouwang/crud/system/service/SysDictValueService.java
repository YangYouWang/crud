package io.github.yangyouwang.crud.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.yangyouwang.common.enums.ResultStatus;
import io.github.yangyouwang.core.exception.CrudException;
import io.github.yangyouwang.crud.system.entity.SysDictType;
import io.github.yangyouwang.crud.system.entity.SysDictValue;
import io.github.yangyouwang.crud.system.mapper.SysDictTypeMapper;
import io.github.yangyouwang.crud.system.mapper.SysDictValueMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * @author yangyouwang
 * @title: SysDictService
 * @projectName crud
 * @description: 字典业务层
 * @date 2021/4/13下午1:11
 */
@Service
public class SysDictValueService extends ServiceImpl<SysDictValueMapper, SysDictValue> {

    @Resource
    private SysDictTypeMapper sysDictTypeMapper;

    /**
     * 根据字典类型获取字典列表
     * @param dictKey 字典key
     * @return 请求列表
     */
    @Transactional(readOnly = true)
    public List<SysDictValue> getDictValues(String dictKey) {
        SysDictType sysDictType = sysDictTypeMapper.findDictByKey(dictKey);
        if (Objects.isNull(sysDictType)) {
            return Collections.emptyList();
        }
        return sysDictType.getDictValues();
    }

    /**
     * 根据字典类型和字典值key获取字典值名称
     * @param dictKey 字典key
     * @param dictValueKey 字典值key
     * @return 字典值名称
     */
    @Transactional(readOnly = true)
    public String getDictValueName(String dictKey,String dictValueKey) {
        SysDictType sysDictType = sysDictTypeMapper.findDictByKey(dictKey);
        List<SysDictValue> dictValues = sysDictType.getDictValues();
        for (SysDictValue dictValue : dictValues) {
            if (dictValue.getDictValueKey().equals(dictValueKey)) {
                return dictValue.getDictValueName();
            }
        }
        throw new CrudException(ResultStatus.DICT_NO_EXIST_ERROR);
    }
}
