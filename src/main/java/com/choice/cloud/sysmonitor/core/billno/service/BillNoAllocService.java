package com.choice.cloud.sysmonitor.core.billno.service;

import java.util.Date;

/**
 * 设备管理-业务单号获取表(DmBillNoAlloc)表服务接口
 *
 * @author makejava
 * @since 2019-12-18 14:24:39
 */
public interface BillNoAllocService {

    /**
     * 通过业务类型获取单号
     *
     * @param billCode 业务类型
     * @return 实例对象
     */
    String getBillNo(int billCode);

    /**
     * 通过业务类型、日期获取单号（此接口能实现示例单据的补号作用）
     *
     * @param billCode 业务类型
     * @param date 日期
     * @return 实例对象
     */
    String getBillNo(int billCode, Date date);

    /**
     * 通过业务类型获取单号
     *
     * @param billCode 业务类型，默认取今天的
     * @param prefix 是否带业务前缀
     * @return 实例对象
     */
    String getBillNo(int billCode, boolean prefix);

    /**
     * 通过业务类型、日期获取单号（此接口能实现示例单据的补号作用）
     *
     * @param billCode 业务类型
     * @param date 日期
     * @param prefix 是否带业务前缀
     * @return 实例对象
     */
    String getBillNo(int billCode, Date date, boolean prefix);

    /**
     * 从数据库获取单号
     * 
     * @param billCode
     * @param prefix
     * @param isPrefix
     * @param key
     * @param date
     * @return
     */
    String getBillNoFromDb(int billCode, String prefix, boolean isPrefix, String key, Date date);

}
