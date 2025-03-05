package io.github.yangyouwang.common.base.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author yangyouwang
 * @title: XmSelectNode
 * @projectName crud
 * @description: 下拉框节点类
 * @date 2021/3/2910:47 AM
 */
@Data
@ApiModel(value="下拉框节点", description="下拉框节点")
public class XmSelectNode {

    /**
     * id
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 值
     */
    @ApiModelProperty(value = "值")
    private Long value;

    /**
     * 是否展示
     */
    @ApiModelProperty(value = "是否展示")
    private Boolean disabled = false;

    /**
     * 是否选中
     */
    @ApiModelProperty(value = "是否选中")
    private Boolean selected = false;
}
