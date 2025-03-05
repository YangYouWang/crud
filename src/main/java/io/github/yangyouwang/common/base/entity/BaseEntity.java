package io.github.yangyouwang.common.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

/**
 * Entity基类
 * 
 * @author crud
 */
@Data
@ApiModel(value="Entity基类", description="Entity基类")
public abstract class BaseEntity {

    /** 主键id */
    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 创建者 */
    @ApiModelProperty(value = "创建者")
    @TableField(value = "create_by",fill = FieldFill.INSERT)
    private String createBy;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    /** 更新者 */
    @ApiModelProperty(value = "更新者")
    @TableField(value = "update_by",fill = FieldFill.UPDATE)
    private String updateBy;

    /** 更新时间 */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;

    /** 逻辑删除 0 否、1 是 */
    @ApiModelProperty(value = "逻辑删除 0 否、1 是")
    @TableLogic
    private Integer deleted;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remark;
}