package com.choice.cloud.sysmonitor.core.distributlock;


import com.choice.cloud.sysmonitor.core.bean.BusinessException;
import com.choice.cloud.sysmonitor.core.enums.ResponseCodeEnum;

/**
 * @author zhaojufei
 */
public interface DistributedLockCallback<T> {

    /**
     * 调用者必须在此方法中实现需要加分布式锁的业务逻辑
     */
    T process();

    /**
     * 默认加锁失败时执行方法。
     */
    default T fail() {
        throw new BusinessException(ResponseCodeEnum.NOT_GET_LOCK);
    }

    /**
     * 得到分布式锁Key
     */
    String getLockName();

}
