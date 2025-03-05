package io.github.yangyouwang.module.code.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("代码生成DTO")
public class BuildDTO {

    /**
     * 作者
     */
    @ApiModelProperty("作者")
    private String author;

    /**
     * 模块名
     */
    @ApiModelProperty("模块名")
    private String moduleName;

    /**
     * 表名
     */
    @ApiModelProperty("表名")
    private String tableName;

    /**
     * 菜单ID
     */
    @ApiModelProperty("菜单ID")
    private Long menuId;
}
