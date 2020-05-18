package com.choice.cloud.sysmonitor.core.formrepeat.annotation;

import java.lang.annotation.*;

/**
 * 
 * 防重复提交token字段注解
 * 使用了该注解的字段将会被视作token
 * 
 * @author zhaojufei
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UnRepeatSubmitTokenField {

}
