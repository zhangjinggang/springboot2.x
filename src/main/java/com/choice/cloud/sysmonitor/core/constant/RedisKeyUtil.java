package com.choice.cloud.sysmonitor.core.constant;

/**
 * redis key都必须在此类中定义，使用：做命名空间分割符，确保自己定义的Key不会覆盖别人的Key。
 *
 * @author zhaojufei
 */
public class RedisKeyUtil {
    public static final String PREFIX = "choice:sys-monitor:";

    /**
     * 获取业务单号分布式锁
     */
    public static String getBillNoLockKey(int billType) {
        StringBuilder builder = new StringBuilder(PREFIX);
        builder.append("billNo:");
        builder.append(billType);
        return builder.toString();
    }

    /**
     * 获取用户form表单操作分布式锁key
     */
    public static String getUserFormOpsLockKey(String tenantId, String userId) {
        return PREFIX + "userFormOps:" + tenantId + ":" + userId;
    }

    /**
     * 获取用户form表单操作TokenMap分布式锁key
     */
    public static String getUserFormOpsTokenMapLockKey(String tenantId, String userId) {
        return PREFIX + "UserFormOpsTokenMap:" + tenantId + ":" + userId;
    }

    /**
     * 获取用户form表单操作TokenMap分布式锁key
     */
    public static String getUserFormOpsIsLockedLockKey(String tenantId, String userId) {
        return PREFIX + "UserFormOpsIsLocked:" + tenantId + ":" + userId;
    }
}
