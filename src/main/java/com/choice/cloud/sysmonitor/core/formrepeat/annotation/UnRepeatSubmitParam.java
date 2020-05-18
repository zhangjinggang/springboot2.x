package com.choice.cloud.sysmonitor.core.formrepeat.annotation;

import java.lang.annotation.*;

/**
 * 防重复提交参数注解
 * 
 * @author zhaojufei
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UnRepeatSubmitParam {

}
