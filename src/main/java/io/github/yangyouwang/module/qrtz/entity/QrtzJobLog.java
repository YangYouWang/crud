package io.github.yangyouwang.module.qrtz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yangyouwang.common.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 任务日志
 * </p>
 *
 * @author yangyouwang
 * @since 2022-10-26
 */
@Data
@TableName("qrtz_job_log")
@ApiModel(value="JobLog对象", description="任务日志")
public class QrtzJobLog extends BaseEntity {

    @ApiModelProperty(value = "任务外键")
    private Long jobId;

    @ApiModelProperty(value = "任务名称")
    private String jobName;

    @ApiModelProperty(value = "任务组名")
    private String jobGroup;

    @ApiModelProperty(value = "类名称")
    private String jobClassName;

    @ApiModelProperty(value = "日志信息")
    private String taskMessage;

    @ApiModelProperty(value = "执行状态：0 正常、1 失败")
    private String status;

    @ApiModelProperty(value = "异常信息")
    private String exceptionInfo;
}
