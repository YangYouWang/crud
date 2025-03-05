package io.github.yangyouwang.common.base.domain;

import io.github.yangyouwang.common.enums.ResultStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * @author yangyouwang
 * @title: Result
 * @projectName crud
 * @description: 返回体类
 * @date 2021/3/209:12 PM
 */
@Getter
@ToString
@ApiModel(value="响应返回体", description="响应返回体")
public class Result<T> {

    /** 业务错误码 */
    @ApiModelProperty(value = "业务错误码")
    private Integer code;
    /** 信息描述 */
    @ApiModelProperty(value = "信息描述")
    private String message;
    /** 返回参数 */
    @ApiModelProperty(value = "返回参数")
    private T data;

    private Result(ResultStatus resultStatus, T data) {
        this.code = resultStatus.code;
        this.message = resultStatus.message;
        this.data = data;
    }

    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 返回成功消息
     * @return 成功消息
     */
    public static <T> Result<T> success() {
        return Result.success(ResultStatus.SUCCESS.message);
    }

    /**
     * 返回成功消息
     * @param message 内容
     * @return 成功消息
     */
    public static <T> Result<T> success(String message) {
        return Result.success(message, null);
    }

    /**
     * 返回成功消息
     * @param data 数据对象
     * @return 成功消息
     */
    public static <T> Result<T> success(T data) {
        return Result.success(ResultStatus.SUCCESS.message,data);
    }

    /**
     * 返回成功消息
     * @param message 内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static <T> Result<T> success(String message,T data) {
        return new Result<>(ResultStatus.SUCCESS.code, message ,data);
    }

    /**
     * 返回错误消息
     * @return 错误消息
     */
    public static <T> Result<T> failure() {
        return Result.failure(ResultStatus.ERROR.message);
    }

    /**
     * 返回错误消息
     * @param message 内容
     * @return 错误消息
     */
    public static <T> Result<T> failure(String message) {
        return Result.failure(message,null);
    }

    /**
     * 返回错误消息
     * @param data 数据对象
     * @return 错误消息
     */
    public static <T> Result<T> failure(T data) {
        return Result.failure(ResultStatus.ERROR.message, data);
    }


    /**
     * 返回错误消息
     * @param message 内容
     * @param data 数据对象
     * @return 错误消息
     */
    public static <T> Result<T> failure(String message,T data) {
        return new Result<>(ResultStatus.ERROR.code, message,data);
    }

    /***
     * 返回自定义状态
     * @param code 状态码
     * @param message 消息
     * @return 消息
     */
    public static <T> Result<T> ok(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}