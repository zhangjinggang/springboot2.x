package com.choice.cloud.sysmonitor.task;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 动态指定task任务执行时间基础类
 */
@Slf4j
@Service
@EnableScheduling
public abstract class BaseScheduler implements SchedulingConfigurer {

    protected abstract void execute();

    protected abstract String getCron();

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(() -> execute(), triggerContext -> {
            String cron = getCron();
            if (StringUtils.isEmpty(cron)) {
                log.error("cron表达式为空，任务停止");
                return null;
            }
            // 任务触发，可修改任务的执行周期
            CronTrigger trigger = new CronTrigger(cron);
            Date nextExec = trigger.nextExecutionTime(triggerContext);
            return nextExec;
        });
    }
}
