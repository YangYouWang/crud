package io.github.yangyouwang.core.web.exception;

import io.github.yangyouwang.common.domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * @author yangyouwang
 * @title: GlobalExceptionHandler
 * @projectName crud
 * @description: 全局异常处理
 * @date 2021/3/209:31 PM
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 参数校验错误
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result handlerMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        log.error("参数校验错误{}", e.getBindingResult());
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            sb.append(fieldError.getDefaultMessage() + ";");
        }
        return Result.failure(sb.toString());
    }

    /**
     * 参数校验错误
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Result handlerConstraintViolationException(ConstraintViolationException e) {
        log.error("参数校验错误{}", e.getLocalizedMessage());
        String message = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));
        return Result.failure(message);
    }

    /**
     * 抛出自定义异常
     */
    @ExceptionHandler(value = CrudException.class)
    @ResponseBody
    public Result exceptionHandler(CrudException e){
        log.error("自定义异常信息！原因是:",e);
        return Result.ok(e.getCode(), e.getMessage());
    }

    /**
     * 处理其他异常信息
     */
    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public Result exceptionHandler(Throwable e){
        log.error("其他异常信息！原因是:",e);
        return Result.failure(e.getMessage());
    }
}
