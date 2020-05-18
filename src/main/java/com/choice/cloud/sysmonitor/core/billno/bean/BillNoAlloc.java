package com.choice.cloud.sysmonitor.core.billno.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 业务单号获取表实体类
 *
 * @author makejava
 * @since 2019-12-18 14:24:37
 */
@Data
public class BillNoAlloc implements Serializable {
    private static final long serialVersionUID = 546992813478722952L;

    /**
     * 单据类型，数字形式，唯一
     */
    private Integer billCode;

    /**
     * 做单号的前缀
     */
    private String prefix;

    /**
     * 申请到的最小号
     */
    private Integer startNo;

    /**
     * 申请到的最大号
     */
    private Integer endNo;

    /**
     * 业务时间
     */
    private Date bussDate;

}
