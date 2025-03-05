package io.github.yangyouwang.module.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.github.yangyouwang.common.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yangyouwang
 * @title: CrudLog
 * @projectName crud
 * @description: 异常日志
 * @date 2021/4/19:50 AM
 */
@Data
@TableName("sys_log")
@ApiModel(value="SysLog对象", description="异常日志表")
public class SysLog extends BaseEntity {
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;
    /**
     * 业务类型
     */
    @ApiModelProperty(value = "业务类型")
    private String businessType;
    /**
     * 包名称
     */
    @ApiModelProperty(value = "包名称")
    private String packageName;
    /**
     * 类名称
     */
    @ApiModelProperty(value = "类名称")
    private String className;
    /**
     * 方法名称
     */
    @ApiModelProperty(value = "方法名称")
    private String methodName;
    /**
     * 参数名
     */
    @ApiModelProperty(value = "参数名")
    private String argsName;
    /**
     * 参数值
     */
    @ApiModelProperty(value = "参数值")
    private String argsValue;
    /**
     * 异常类型
     */
    @ApiModelProperty(value = "异常类型")
    private String exceptionName;
    /**
     * 错误信息
     */
    @ApiModelProperty(value = "错误信息")
    private String errMsg;
    /**
     * 异常堆栈信息
     */
    @ApiModelProperty(value = "异常堆栈信息")
    private String stackTrace;
}
