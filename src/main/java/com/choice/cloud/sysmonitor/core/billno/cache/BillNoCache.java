package com.choice.cloud.sysmonitor.core.billno.cache;


import com.choice.cloud.sysmonitor.core.billno.bean.BillNoAlloc;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 单据号本地缓存
 *
 * @author zhaojufei
 */
@Component
public class BillNoCache {

    /**
     * 单据号按照天发放，缓存有效期设置为一天。
     * 即便是到了第二天，业务侧传的参数也是第二天，不会生成昨天的单号。
     * 所以缓存有效期一天满足需求，不会有问题。
     */
    private static Cache<String, BillNoAlloc> cache = CacheBuilder.newBuilder().expireAfterWrite(1L, TimeUnit.DAYS)
            .maximumSize(100L).build();

    public BillNoAlloc getCache(String key) {
        return cache.getIfPresent(key);
    }

    public void setCache(String key, BillNoAlloc obj) {
        cache.put(key, obj);
    }

    public void removeCache(String key) {
        cache.invalidate(key);
    }

    public void removeAll() {
        cache.invalidateAll();
    }

    public long getSize() {
        return cache.size();
    }
}
