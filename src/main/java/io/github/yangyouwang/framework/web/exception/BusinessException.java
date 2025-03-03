package io.github.yangyouwang.framework.web.exception;

import io.github.yangyouwang.common.enums.ResultStatus;

/**
 * @author yangyouwang
 * @title: BusinessException
 * @projectName crud
 * @description: 自定义业务异常
 * @date 2021/3/3010:10 PM
 */
public class BusinessException extends RuntimeException {

    /**
     * 业务异常码
     * */
    public Integer code;
    /**
     * 业务异常信息描述
     *  */
    public String message;

    public BusinessException(ResultStatus resultStatus) {
        super(resultStatus.getMessage());
        this.code = resultStatus.getCode();
        this.message = resultStatus.getMessage();
    }

    public BusinessException(String message) {
        super(message);
        this.code = ResultStatus.ERROR.getCode();
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
