package com.choice.cloud.sysmonitor.common.util;

import java.util.UUID;

/**
 * uuid生成
 *
 * @author zhaojufei
 */
public class UUIDGenerator {

    /**
     * 获取带下划线的UUID
     * 
     * @return
     */
    public static String getUU_ID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取不带下划线的UUID
     * 
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
