package io.github.yangyouwang.framework.util.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import io.github.yangyouwang.common.annotation.DictType;
import io.github.yangyouwang.framework.util.SpringUtils;
import io.github.yangyouwang.module.system.entity.SysDictValue;
import io.github.yangyouwang.module.system.service.SysDictValueService;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 通用字典数据转换器
 */
public class BaseDictDataConverter implements Converter<String> {

    private static SysDictValueService sysDictValueService = SpringUtils.getBean(SysDictValueService.class);

    @Override
    public Class supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public String convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        Field field = excelContentProperty.getField();
        if (field.isAnnotationPresent(DictType.class)) {
            DictType dictType = field.getAnnotation(DictType.class);
            List<SysDictValue> dictValues = sysDictValueService.getDictValues(dictType.key());
            for (SysDictValue dictValue : dictValues) {
                if(dictValue.getDictValueName().equals(cellData.getStringValue())){
                    return dictValue.getDictValueKey();
                }
            }
        }
        return null;
    }

    @Override
    public CellData convertToExcelData(String s, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        Field field = excelContentProperty.getField();
        if (field.isAnnotationPresent(DictType.class)) {
            DictType dictType = field.getAnnotation(DictType.class);
            List<SysDictValue> dictValues = sysDictValueService.getDictValues(dictType.key());
            for (SysDictValue dictValue : dictValues) {
                if(dictValue.getDictValueKey().equals(s)){
                    return new CellData(dictValue.getDictValueName());
                }
            }
        }
        return null;
    }
}
