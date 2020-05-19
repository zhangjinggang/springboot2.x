package com.choice.cloud.sysmonitor.core.properties;

import lombok.Data;

/**
 * 定时任务相关配置
 */
@Data
public class MailRemindTaskProperties {

    /**
     * 是否启用定时任务，默认启用
     */
    private Boolean enable = true;

    /**
     * 邮件提醒cron表达式
     */
    private String cron;

    /**
     * 邮件主题
     */
    private String subject = "系统监控报告";

}
