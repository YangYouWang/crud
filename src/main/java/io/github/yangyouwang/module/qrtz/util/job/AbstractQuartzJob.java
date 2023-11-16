package io.github.yangyouwang.module.qrtz.util.job;

import io.github.yangyouwang.common.constant.ConfigConsts;
import io.github.yangyouwang.core.util.SpringUtils;
import io.github.yangyouwang.module.qrtz.entity.QrtzJob;
import io.github.yangyouwang.module.qrtz.entity.QrtzJobLog;
import io.github.yangyouwang.module.qrtz.service.JobLogService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Description: JOB抽象类 <br/>
 * date: 2022/10/30 1:33<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
@Slf4j
public abstract class AbstractQuartzJob implements Job {

    /**
     * 线程本地变量
     */
    private static final ThreadLocal<Date> THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        QrtzJob task = (QrtzJob) context.getMergedJobDataMap().get(ConfigConsts.TASK_PROPERTIES);
        try {
            before(context, task);
            doExecute(context, task);
            after(context, task, null);
        } catch (Exception ex) {
            log.error("定时任务执行异常：{}", ex.getMessage());
            after(context, task, ex);
        }
    }

    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param task 任务
     */
    protected void before(JobExecutionContext context, QrtzJob task) {
        THREAD_LOCAL.set(new Date());
    }

    protected void after(JobExecutionContext context, QrtzJob task, Exception ex) {
        Date startTime = THREAD_LOCAL.get();
        THREAD_LOCAL.remove();
        QrtzJobLog taskLog = new QrtzJobLog();
        taskLog.setJobId(task.getId());
        taskLog.setJobName(task.getJobName());
        taskLog.setJobGroup(task.getJobGroup());
        taskLog.setJobClassName(task.getJobClassName());
        taskLog.setCreateTime(startTime);
        taskLog.setUpdateTime(new Date());
        long runMs = taskLog.getUpdateTime().getTime() - taskLog.getCreateTime().getTime();
        taskLog.setTaskMessage(task.getJobName() + " 总共耗时：" + TimeUnit.MILLISECONDS.toSeconds(runMs) + "秒");
        if (null != ex) {
            taskLog.setStatus(ConfigConsts.ERROR_STATUS);
            taskLog.setExceptionInfo(ex.getMessage());
        } else {
            taskLog.setStatus(ConfigConsts.SUCCESS_STATUS);
        }
        SpringUtils.getBean(JobLogService.class).save(taskLog);
    }

    /**
     * 执行方法
     *
     * @param context 工作执行上下文对象
     * @param task 系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, QrtzJob task) throws Exception;
}
