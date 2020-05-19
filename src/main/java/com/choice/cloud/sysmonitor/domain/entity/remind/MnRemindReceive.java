package com.choice.cloud.sysmonitor.domain.entity.remind;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 邮件提醒接收邮件地址表(MnRemindConfReceive)实体类
 *
 * @author ZhaoJuFei
 * @since 2020-05-19 15:18:50
 */
@Data
public class MnRemindReceive implements Serializable {
    private static final long serialVersionUID = 158093354303164424L;
    
    /**
     * 配置项Id
     */
    private String confId;

    /**
     * 接收人姓名
     */
    private String receiveName;

    /**
     * 接收方式：MC主送，CC抄送
     */
    private String receiveType;

    /**
     * 接收邮件地址
     */
    private String receiveMail;

    /**
     * 创建人ID
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人ID
     */
    private String updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 逻辑删除标识：0未删除，1已删除
     */
    private Integer deleteFlag;
}