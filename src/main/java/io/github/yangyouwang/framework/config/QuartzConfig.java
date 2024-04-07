package io.github.yangyouwang.framework.config;

import io.github.yangyouwang.module.qrtz.util.job.JobFactory;
import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

/**
 * Description: Quartz 定时任务配置 <br/>
 * date: 2022/10/30 1:19<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
@Configuration
public class QuartzConfig {

    private final JobFactory jobFactory;

    public QuartzConfig(JobFactory jobFactory) {
        this.jobFactory = jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        try {
            factory.setSchedulerName("Scheduler");
            factory.setQuartzProperties(quartzProperties());
            // 延时启动
            factory.setStartupDelay(1);
            factory.setApplicationContextSchedulerContextKey("applicationContextKey");
            // 可选，QuartzScheduler
            // 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
            factory.setOverwriteExistingJobs(true);
            factory.setJobFactory(jobFactory);
            // 设置自动启动，默认为true
            factory.setAutoStartup(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        // quartz参数
        Properties prop = new Properties();
        // 线程池配置
        prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        prop.put("org.quartz.threadPool.threadCount", "20");
        prop.put("org.quartz.threadPool.threadPriority", "5");
        return prop;
    }

    @Bean(name = "scheduler")
    public Scheduler scheduler() {
        return schedulerFactoryBean().getScheduler();
    }
}
