package com.choice.cloud.sysmonitor.config;

import cn.hutool.core.lang.Snowflake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 三码合一陪配置项
 */
@Slf4j
@Configuration
public class SysMonitorProperties {

    @Bean
    @ConditionalOnMissingBean({Snowflake.class})
    public Snowflake snowflake() {
        return new Snowflake(2L, 2L);
    }

}
