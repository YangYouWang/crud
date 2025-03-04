package io.github.yangyouwang.framework.aspect;

import io.github.yangyouwang.common.annotation.CrudLog;
import io.github.yangyouwang.module.system.mapper.SysLogMapper;
import io.github.yangyouwang.module.system.entity.SysLog;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * @author yangyouwang
 * @title: SysLogAspect
 * @projectName crud
 * @description: 日志切面
 * @date 2021/4/18:37 AM
 */
@Aspect
@Component
public class SysLogAspect {

    @Resource
    private SysLogMapper sysLogMapper;

    /**
     * 切入点
     */
    @Pointcut(value = "@annotation(io.github.yangyouwang.common.annotation.CrudLog)")
    private void logPointCut() {}

    /**
     * 方法返回后的通知
     */
    @AfterReturning(value = "logPointCut() && @annotation(crudLog)", returning = "obj")
    public void doAfterReturning(JoinPoint joinPoint, CrudLog crudLog, Object obj) {
        SysLog sysLog = new SysLog();
        sysLog.setTitle(crudLog.title());
        sysLog.setBusinessType(crudLog.businessType().type);
        sysLog.setClassName(joinPoint.getTarget().getClass().getSimpleName());
        sysLog.setMethodName(joinPoint.getSignature().getName());
        sysLog.setPackageName(joinPoint.getSignature().getDeclaringTypeName());
        sysLog.setArgsValue(StringUtils.join(Arrays.stream(joinPoint.getArgs()).toArray(Object[]::new)));
        sysLog.setArgsName(StringUtils.join(((MethodSignature)joinPoint.getSignature()).getParameterNames()));
        sysLogMapper.insert(sysLog);
    }

    /**
     * 抛出异常后的通知
     */
    @AfterThrowing(value = "logPointCut() && @annotation(crudLog)", throwing = "e")
    private void doAfterThrowing(JoinPoint joinPoint, CrudLog crudLog, Exception e) {
        SysLog sysLog = new SysLog();
        sysLog.setTitle(crudLog.title());
        sysLog.setBusinessType(crudLog.businessType().type);
        sysLog.setClassName(joinPoint.getTarget().getClass().getSimpleName());
        sysLog.setMethodName(joinPoint.getSignature().getName());
        sysLog.setPackageName(joinPoint.getSignature().getDeclaringTypeName());
        sysLog.setArgsValue(StringUtils.join(Arrays.stream(joinPoint.getArgs()).toArray(Object[]::new)));
        sysLog.setArgsName(StringUtils.join(((MethodSignature)joinPoint.getSignature()).getParameterNames()));
        StringWriter stackTraceWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTraceWriter, true));
        sysLog.setStackTrace(stackTraceWriter.toString());
        sysLog.setErrMsg(e.getMessage());
        sysLog.setExceptionName(e.toString());
        sysLogMapper.insert(sysLog);
    }
}
