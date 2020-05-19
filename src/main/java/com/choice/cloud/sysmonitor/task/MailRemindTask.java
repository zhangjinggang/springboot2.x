package com.choice.cloud.sysmonitor.task;

import com.choice.cloud.sysmonitor.biz.remind.MnRemindConfService;
import com.choice.cloud.sysmonitor.core.constant.RedisKeyUtil;
import com.choice.cloud.sysmonitor.core.properties.MailRemindTaskProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 邮件提醒定时任务
 */
@Slf4j
@Component
public class MailRemindTask extends BaseScheduler {

    @Autowired
    private MailRemindTaskProperties mailRemindTaskProperties;

    @Autowired
    private MnRemindConfService mnRemindConfService;

    @Override
    protected void execute() {
        if (mailRemindTaskProperties.getEnable()) {
            mnRemindConfService.startTask(RedisKeyUtil.getMailRemindLoclKey());
        } else {
            log.warn("邮件提醒定时任务已关闭，定时任务不做任何逻辑直接退出。");
        }
    }

    @Override
    protected String getCron() {
        return mailRemindTaskProperties.getCron();
    }
}
