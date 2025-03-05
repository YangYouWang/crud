package io.github.yangyouwang.module.qrtz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yangyouwang.common.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 任务表
 * </p>
 *
 * @author yangyouwang
 * @since 2022-07-30
 */
@Data
@TableName("qrtz_job")
@ApiModel(value="Job对象", description="任务表")
public class QrtzJob extends BaseEntity {

    @ApiModelProperty(value = "任务名字")
    private String jobName;

    @ApiModelProperty(value = "任务组")
    private String jobGroup;

    @ApiModelProperty(value = "cron表达式")
    private String cron;

    @ApiModelProperty(value = "类名称")
    private String jobClassName;

    @ApiModelProperty(value = "是否启用 Y 启用 N 禁用")
    private String enabled;
}
