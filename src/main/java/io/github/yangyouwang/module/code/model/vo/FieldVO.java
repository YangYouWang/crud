package io.github.yangyouwang.module.code.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("表字段")
public class FieldVO {

    /**
     * 列名
     */
    private String columnName;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 列类型
     */
    private String typeName;

    /**
     * 列长度
     */
    private String columnSize;
}
