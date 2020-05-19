package com.choice.cloud.sysmonitor.domain.entity.remind;

import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 邮件提醒配置表(MnRemindConf)实体类
 *
 * @author ZhaoJuFei
 * @since 2020-05-19 09:14:43
 */
@Data
public class MnRemindConf implements Serializable {
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
     * 有些HTTP地址是需要用户名、密码的
     */
    private String userName;

    /**
     * 有些HTTP地址是需要用户名、密码的
     */
    private String passWord;

    /**
     * 配置简述
     */
    private String confDesc;

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

    /**
     * 邮件接收者
     */
    private List<MnRemindReceive> receiveList;

    /**
     * 获取邮件地址，目前发送接口暂不支持主送、抄送，全部当做收件人发送。
     * 后续可以再写两个子程序，分别获取主送，抄送列表，根据receiveType区分。
     *
     * @return
     */
    public List<String> getRecEmailList() {
        if (CollectionUtils.isNotEmpty(receiveList)) {
            return receiveList.stream().map(MnRemindReceive::getReceiveMail).collect(Collectors.toList());
        }
        return null;
    }


}