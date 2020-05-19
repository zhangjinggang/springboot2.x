package com.choice.cloud.sysmonitor.controller;

import com.choice.cloud.sysmonitor.biz.remind.MnRemindConfService;
import com.choice.cloud.sysmonitor.core.bean.ResponseData;
import com.choice.cloud.sysmonitor.core.constant.CommonConstant;
import com.choice.cloud.sysmonitor.core.constant.RedisKeyUtil;
import com.choice.cloud.sysmonitor.domain.dto.PageMnRemindConfDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 邮件提醒配置表(MnRemindConf)表控制层
 *
 * @author ZhaoJuFei
 * @since 2020-05-19 09:18:55
 */
@RestController
@RequestMapping(TaskController.URI)
public class TaskController {
    /**
     * 本类接口URI前缀
     */
    public static final String URI = CommonConstant.CONTEXT_PATH + "/task";

    /**
     * 服务对象
     */
    @Autowired
    private MnRemindConfService mnRemindConfService;

    /**
     * 无条件的启动定时任务
     *
     * @return
     */
    @RequestMapping("/mail-remind/start")
    public ResponseData startMailRemind() {
        this.mnRemindConfService.startTask(RedisKeyUtil.getMailRemindLoclKey());
        return ResponseData.getSuccessRes();
    }

    /**
     * 有条件的启动定时任务：可指定收件地址、查询的配置项等
     *
     * @param pageMnRemindConfDto
     * @return
     */
    @PostMapping("/mail-remind/start-by-condition")
    public ResponseData startByCondition(@RequestBody @Validated PageMnRemindConfDto pageMnRemindConfDto) {
        this.mnRemindConfService.startTask(RedisKeyUtil.getMailRemindLoclKey(), pageMnRemindConfDto);
        return ResponseData.getSuccessRes();
    }

}