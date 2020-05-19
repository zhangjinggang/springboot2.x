package com.choice.cloud.sysmonitor.biz.remind;


import com.choice.cloud.sysmonitor.domain.dto.PageMnRemindConfDto;

/**
 * 邮件提醒配置表(MnRemindConf)表服务接口
 *
 * @author ZhaoJuFei
 * @since 2020-05-19 09:17:09
 */
public interface MnRemindConfService {
    /**
     * 启动定时任务处理逻辑
     */
    void startTask(String lockKey);

    /**
     * 接收一个参数dto启动定时任务
     */
    void startTask(String lockKey, PageMnRemindConfDto pageMnRemindConfDto);

}