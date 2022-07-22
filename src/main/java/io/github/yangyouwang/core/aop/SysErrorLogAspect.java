package io.github.yangyouwang.core.aop;

import io.github.yangyouwang.common.annotation.CrudLog;
import io.github.yangyouwang.crud.system.mapper.SysLogMapper;
import io.github.yangyouwang.crud.system.entity.SysLog;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;

/**
 * @author yangyouwang
 * @title: SysLogAspect
 * @projectName crud
 * @description: 错误日志切面
 * @date 2021/4/18:37 AM
 */
@Aspect
@Component
public class SysErrorLogAspect {

    @Resource
    private SysLogMapper sysLogMapper;

    /**
     * 切入点
     */
    @Pointcut(value = "@annotation(io.github.yangyouwang.common.annotation.CrudLog)")
    private void logPointCut() {

    }

    /**
     * 抛出异常后的通知
     */
    @AfterThrowing(value = "logPointCut() && @annotation(crudLog)", throwing = "e")
    private void logAfterThrowing(JoinPoint joinPoint, CrudLog crudLog, Exception e) {
        SysLog sysLog = new SysLog();
        // 类名称
        String className = joinPoint.getTarget().getClass().getSimpleName();
        sysLog.setClassName(className);
        //方法名称
        String methodName = joinPoint.getSignature().getName();
        sysLog.setMethodName(methodName);
        // 包名称
        String packageName = joinPoint.getSignature().getDeclaringTypeName();
        sysLog.setPackageName(packageName);
        // 参数值
        Object[] argsValue = Arrays.stream(joinPoint.getArgs()).toArray(Object[]::new);
        sysLog.setArgsValue(StringUtils.join(argsValue));
        // 参数名
        String[] argsNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();
        sysLog.setArgsName(StringUtils.join(argsNames));
        StringWriter stackTraceWriter = new StringWriter();
        //异常堆栈信息
        e.printStackTrace(new PrintWriter(stackTraceWriter, true));
        sysLog.setStackTrace(stackTraceWriter.toString());
        //错误信息
        String errMsg = e.getMessage();
        sysLog.setErrMsg(errMsg);
        //异常类型
        String exceptionName = e.toString();
        sysLog.setExceptionName(exceptionName);
        sysLog.setCreateTime(new Date());
        sysLogMapper.insert(sysLog);
    }
}