package com.choice.cloud.sysmonitor.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaojufei
 */
@RestController
@Slf4j
public class HealthController {
    /**
     * 健康检查接口
     *
     * @return
     */
    @RequestMapping("/ping")
    public String ping() {
        return "pong";
    }

}
