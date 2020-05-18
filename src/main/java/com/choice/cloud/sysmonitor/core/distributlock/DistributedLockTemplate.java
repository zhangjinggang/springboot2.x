package com.choice.cloud.sysmonitor.core.distributlock;

import com.choice.cloud.sysmonitor.core.bean.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 带回调的分布式锁客户端
 * @author zhaojufei
 */
@Component
@Slf4j
public class DistributedLockTemplate {

    long DEFAULT_WAIT_TIME = 60L;
    long DEFAULT_TIMEOUT = 60L;

    TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    @Autowired
    private RedissonClient redissonClient;

    public DistributedLockTemplate() {
    }

    /**
     * 直接Lock模式
     * 
     * @param callback
     * @param fairLock
     * @param <T>
     * @return
     */
    public <T> T lock(DistributedLockCallback<T> callback, boolean fairLock) {
        return lock(callback, DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT, fairLock);
    }

    /**
     * 直接Lock模式
     * 
     * @param callback
     * @param leaseTime
     * @param timeUnit
     * @param fairLock
     * @param <T>
     * @return
     */
    public <T> T lock(DistributedLockCallback<T> callback, long leaseTime, TimeUnit timeUnit, boolean fairLock) {
        RLock lock = getLock(callback.getLockName(), fairLock);
        try {
            lock.lock(leaseTime, timeUnit);
            boolean locked = Thread.currentThread().isInterrupted() ? false : true;
            if (locked) {
                return callback.process();
            } else {
                return callback.fail();
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * try Lock模式
     * 
     * @param callback
     * @param fairLock
     * @param <T>
     * @return
     */
    public <T> T tryLock(DistributedLockCallback<T> callback, boolean fairLock) {
        return tryLock(callback, DEFAULT_WAIT_TIME, DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT, fairLock);
    }

    /**
     * try Lock模式
     * 
     * @param callback
     * @param waitTime
     * @param leaseTime
     * @param timeUnit
     * @param fairLock
     * @param <T>
     * @return
     */
    public <T> T tryLock(DistributedLockCallback<T> callback, long waitTime, long leaseTime, TimeUnit timeUnit,
            boolean fairLock) {
        RLock lock = getLock(callback.getLockName(), fairLock);

        try {
            boolean locked = lock.tryLock(waitTime, leaseTime, timeUnit);
            if (locked) {
                return callback.process();
            } else {
                return callback.fail();
            }
        } catch (InterruptedException e) {
            log.error("获取分布式锁时出现异常，errMsg = {}, error stack = {}", e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private RLock getLock(String lockName, boolean fairLock) {
        RLock lock;
        if (fairLock) {
            lock = redissonClient.getFairLock(lockName);
        } else {
            lock = redissonClient.getLock(lockName);
        }
        return lock;
    }

}
