package com.choice.cloud.sysmonitor.core.billno.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备管理-业务单号获取表实体类
 *
 * @author makejava
 * @since 2019-12-18 14:24:37
 */
@Data
public class BillNoAllocEntity implements Serializable {

    private static final long serialVersionUID = 546992813478722952L;

    /**
     * UUID
     */
    private String allocId;

    /**
     * 单据类型，数字形式，唯一
     */
    private Integer billCode;

    /**
     * 单号的前缀
     */
    private String prefix;

    /**
     * 业务日期
     */
    private Date bussDate;

    /**
     * 已发放的最大号
     */
    private Integer maxId;

    /**
     * 发号步长
     */
    private Integer step;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

}
