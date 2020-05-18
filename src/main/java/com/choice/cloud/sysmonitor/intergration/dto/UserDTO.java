package com.choice.cloud.sysmonitor.intergration.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhaojufei
 */
@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -3049291182125052829L;
    /**
     * 员工id
     */
    private String id;
    /**
     * 商户id
     */
    private String tenantId;

    /**
     * 商户名称
     */
    private String tenantName;

    /**
     * 员工姓名
     */
    private String realname;
    /**
     * 头像地址
     */
    private String headImg;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 姓别 1男2女
     */
    private Integer gender;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 岗位ID
     */
    private String postId;

    /**
     *
     **/
    private List<String> roleIds;
    /**
     * 状态：是否停用
     */
    private Integer status;

    private String sourceId;

    private Integer source;

    private Integer deleteFlag;

}
