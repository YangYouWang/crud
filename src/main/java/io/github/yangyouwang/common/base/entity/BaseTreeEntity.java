package io.github.yangyouwang.common.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Tree基类
 * 
 * @author crud
 */
@Data
@ApiModel(value="Tree基类", description="Tree基类")
public class BaseTreeEntity extends BaseEntity {

    /** 上级ID */
    @ApiModelProperty(value = "上级主键")
    private Long parentId;

    /** 上级名称 */
    @ApiModelProperty(value = "上级名称")
    @TableField(exist = false)
    private String parentName;
}