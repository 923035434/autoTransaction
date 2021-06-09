package com.ll.autotransaction.job.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;


//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
//@ComponentScan(basePackages = "com.ll.autotransaction.job")
@Configuration
@EnableScheduling
@Slf4j
public class JobSchedulerConfig {
    public JobSchedulerConfig()
    {
        log.info("加载com.ll.auto_transaction.conf.AppConfig");
    }

    @Bean(name = "taskScheduler")
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        taskScheduler.setThreadNamePrefix("taskExecutor-");
        //用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        //该方法用来设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
        taskScheduler.setAwaitTerminationSeconds(300);
        //taskScheduler.setErrorHandler();
        return taskScheduler;
    }
}