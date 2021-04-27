package io.github.yangyouwang.common.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity基类
 * 
 * @author crud
 */
@Data
public abstract class BaseEntity implements Serializable {

    private static final Long serialVersionUID = 1L;

    /** 创建者 */
    @TableField(value = "create_by",fill = FieldFill.INSERT)
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    /** 更新者 */
    @TableField(value = "update_by",fill = FieldFill.UPDATE)
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;

    /** 备注 */
    private String remark;
}