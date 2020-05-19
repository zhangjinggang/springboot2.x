package com.choice.cloud.sysmonitor.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 邮件提醒配置表(MnRemindConf)实体类
 *
 * @author ZhaoJuFei
 * @since 2020-05-19 09:14:43
 */
@Data
public class PageMnRemindConfDto extends PageDto implements Serializable {
    private static final long serialVersionUID = 595806518741313173L;
    /**
     * 配置项Id
     */
    private String confId;
    /**
     * 配置项名称
     */
    private String confName;
    /**
     * 配置类型：url
     */
    private String confType;
    /**
     * 配置项值
     */
    private String confValue;
    /**
     * 配置简述
     */
    private String confDesc;
    /**
     * 接收邮件地址
     */
    private String receiveMail;

    // 覆盖邮箱地址，如果这个字段有值，则不再使用配置项里的邮箱
    private String replaceMail;

}