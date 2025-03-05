package io.github.yangyouwang.module.code.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 表字段
 * @author yangyouwang
 */
@Data
@ApiModel("表字段")
public class FieldVO {

    /**
     * 列名
     */
    @ApiModelProperty(value = "列名")
    private String columnName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * 列类型
     */
    @ApiModelProperty(value = "列类型")
    private String typeName;

    /**
     * 列长度
     */
    @ApiModelProperty(value = "列长度")
    private String columnSize;
}
