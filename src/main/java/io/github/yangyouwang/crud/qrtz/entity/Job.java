package io.github.yangyouwang.crud.qrtz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yangyouwang.common.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 任务表
 * </p>
 *
 * @author yangyouwang
 * @since 2022-07-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("qrtz_job")
@ApiModel(value="Job对象", description="任务表")
public class Job extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工作名字")
    private String jobName;

    @ApiModelProperty(value = "cron表达式")
    private String cron;

    @ApiModelProperty(value = "类名称")
    private String jobClassName;

    @ApiModelProperty(value = "是否启用 Y 启用 N 禁用")
    private String enabled;
}
