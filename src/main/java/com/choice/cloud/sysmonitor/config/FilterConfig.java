package com.choice.cloud.sysmonitor.config;


import com.choice.cloud.sysmonitor.core.filter.LogParamFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean registFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new LogParamFilter());
        registration.addUrlPatterns("/*");
        registration.setName("LogParamFilter");
        return registration;
    }
}
