package io.github.yangyouwang.module.qrtz.util.job;

import io.github.yangyouwang.common.constant.ConfigConsts;
import io.github.yangyouwang.module.qrtz.entity.QrtzJob;
import org.quartz.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description: 定时任务管理类 <br/>
 * date: 2022/10/30 1:52<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
@Service
public class QuartzManager {

    @Resource
    private Scheduler scheduler;

    /**
     * 得到quartz任务类
     *
     * @param task 执行计划
     * @return 具体执行任务类
     */
    private static Class<? extends Job> getQuartzJobClass(QrtzJob task) {
        return QuartzJobExecution.class;
    }

    /**
     * 添加任务job
     *
     * @param task :任务实体类
     */
    @SuppressWarnings("unchecked")
    public void addJob(QrtzJob task) {
        try {
            // 创建jobDetail实例， 绑定Job实现类
            // 指明job的名称，所在组的名称，以及绑定Job类
            Class<? extends Job> jobClass = getQuartzJobClass(task);
            // 配置任务名称和组构成任务key
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(getJobKey(task.getJobName(), task.getJobGroup())).build();
            // 定义调度规则
            // 使用cornTrigger规则
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(task.getJobName(), task.getJobGroup())
                    .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                    .withSchedule(CronScheduleBuilder.cronSchedule(task.getCron())).startNow().build();
            jobDetail.getJobDataMap().put(ConfigConsts.TASK_PROPERTIES, task);
            // 判断是否存在
            if (scheduler.checkExists(getJobKey(task.getJobName(), task.getJobGroup()))) {
                // 防止创建时存在数据问题 先移除，然后在执行创建操作
                scheduler.deleteJob(getJobKey(task.getJobName(), task.getJobGroup()));
            }
            // 把job和触发器注册到任务调度中
            scheduler.scheduleJob(jobDetail, trigger);
            // 执行调度任务
            if (!scheduler.isShutdown() &&
                    ConfigConsts.SYS_YES.equals(task.getEnabled())) {
                scheduler.start();
            }
            // 暂停调度任务
            if (!ConfigConsts.SYS_YES.equals(task.getEnabled())) {
                scheduler.pauseJob(getJobKey(task.getJobName(), task.getJobGroup()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 构建任务键对象
     */
    public static JobKey getJobKey(String jobName, String jobGroup) {
        return JobKey.jobKey(jobName, jobGroup);
    }

    /**
     * 暂停一个job
     * @param task :任务实体类
     */
    public void pauseJob(QrtzJob task) throws SchedulerException {
        scheduler.pauseJob(getJobKey(task.getJobName(), task.getJobGroup()));
    }

    /**
     * 恢复一个job
     * @param task :任务实体类
     */
    public void resumeJob(QrtzJob task) throws SchedulerException {
        scheduler.resumeJob(getJobKey(task.getJobName(), task.getJobGroup()));
    }

    /**
     * 删除一个job
     * @param task :任务实体类
     */
    public void deleteJob(QrtzJob task) throws SchedulerException {
        scheduler.deleteJob(getJobKey(task.getJobName(), task.getJobGroup()));
    }

    /**
     * 立即触发job
     *
     * @param task :任务实体类
     */
    public void runJobNow(QrtzJob task) throws SchedulerException {
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ConfigConsts.TASK_PROPERTIES, task);
        scheduler.triggerJob(getJobKey(task.getJobName(), task.getJobGroup()), dataMap);
    }

    /**
     * 更新job表达式
     * @param task 任务实体类
     */
    public void updateJobCron(QrtzJob task) throws SchedulerException {
        // 暂停定时任务
        scheduler.pauseJob(getJobKey(task.getJobName(), task.getJobGroup()));
        // 修改定时任务
        TriggerKey triggerKey = TriggerKey.triggerKey(task.getJobName(), task.getJobGroup());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCron());
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        scheduler.rescheduleJob(triggerKey, trigger);
        // 根据状态判断是否要执行任务
        if(ConfigConsts.SYS_YES.equals(task.getEnabled())) {
            this.resumeJob(task);
        } else {
            this.pauseJob(task);
        }
    }
}
