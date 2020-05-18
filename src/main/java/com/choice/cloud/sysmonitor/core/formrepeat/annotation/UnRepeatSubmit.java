package com.choice.cloud.sysmonitor.core.formrepeat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 防重复提交方法注解
 *
 * @author zhaojufei
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * 防重复提交注解，用在方法上
 */
public @interface UnRepeatSubmit {

}
