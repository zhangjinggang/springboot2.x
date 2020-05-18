package com.choice.cloud.sysmonitor.core.billno.service.impl;


import cn.hutool.core.lang.UUID;
import com.choice.cloud.sysmonitor.common.util.DmDateUtil;
import com.choice.cloud.sysmonitor.core.bean.BusinessException;
import com.choice.cloud.sysmonitor.core.billno.bean.BillNoAlloc;
import com.choice.cloud.sysmonitor.core.billno.cache.BillNoCache;
import com.choice.cloud.sysmonitor.core.billno.entity.BillNoAllocEntity;
import com.choice.cloud.sysmonitor.core.billno.enums.BillTypeEnum;
import com.choice.cloud.sysmonitor.core.billno.mapper.BillNoAllocMapper;
import com.choice.cloud.sysmonitor.core.billno.service.BillNoAllocService;
import com.choice.cloud.sysmonitor.core.constant.RedisKeyUtil;
import com.choice.cloud.sysmonitor.core.distributlock.DistributedLockCallback;
import com.choice.cloud.sysmonitor.core.distributlock.DistributedLockTemplate;
import com.choice.cloud.sysmonitor.core.enums.ResponseCodeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * 设备管理-业务单号获取表表服务实现类
 *
 * @author makejava
 * @since 2019-12-18 14:24:39
 */
@Service
public class BillNoAllocServiceImpl implements BillNoAllocService {

    @Autowired
    private BillNoAllocMapper billNoAllocMapper;

    @Autowired
    private BillNoCache billNoCache;

    @Autowired
    private DistributedLockTemplate distributedLockTemplate;

    @Autowired
    private BillNoAllocService self;

    private int step = 2000;

    /**
     * 通过业务类型获取单号，默认取今天的
     *
     * @param billCode 业务类型
     * @return 实例对象
     */
    @Override
    public String getBillNo(int billCode) {
        return this.getBillNo(billCode, new Date(), false);
    }

    /**
     * 通过业务类型、日期获取单号
     *
     * @param billCode 业务类型
     * @param date     是否带业务前缀
     * @return 实例对象
     */
    @Override
    public String getBillNo(int billCode, Date date) {
        return this.getBillNo(billCode, date, false);
    }

    /**
     * 通过业务类型获取单号，默认取今天的
     *
     * @param billCode 业务类型
     * @param isPrefix   是否带业务前缀
     * @return 实例对象
     */
    @Override
    public String getBillNo(int billCode, boolean isPrefix) {
        return this.getBillNo(billCode, new Date(), isPrefix);
    }

    /**
     * 通过业务类型、日期获取单号
     *
     * @param billCode 业务类型
     * @param date     日期
     * @param isPrefix   是否带业务前缀
     * @return 实例对象
     */
    @Override
    public String getBillNo(int billCode, Date date, boolean isPrefix) {
        // 1、获取单据枚举，不存在报错
        BillTypeEnum curEnum = BillTypeEnum.getEnum(billCode);
        if (Objects.isNull(curEnum)) {
            throw new BusinessException(ResponseCodeEnum.BILL_TYPE_NOT_EXISTS);
        }

        // 2、从本地缓存中获取单号，序列号按照步长分发给每台机器，每台机器只需要控制本机的并发即可
        synchronized (curEnum) {
            String key = getBillNoKey(date, billCode);
            BillNoAlloc billNoAllocCache = billNoCache.getCache(key);

            // 如果本地缓存中不存在，需要从DB中获取，需要在多台机器间做分布式锁
            if (Objects.isNull(billNoAllocCache)) {
                return (String) distributedLockTemplate.tryLock(new DistributedLockCallback<Object>() {
                    /**
                     * 调用者必须在此方法中实现需要加分布式锁的业务逻辑
                     */
                    @Override
                    public Object process() {
                        // 主要逻辑提出方法来，主要是为了搞定事务
                        return self.getBillNoFromDb(billCode, curEnum.getPrefix(), isPrefix, key, date);
                    }

                    /**
                     * 得到分布式锁key
                     */
                    @Override
                    public String getLockName() {
                        return RedisKeyUtil.getBillNoLockKey(billCode);
                    }
                }, true);

            } else {
                return getAndSetBillNo(key, billNoAllocCache, isPrefix);
            }

        }
    }

    /**
     * 返回key
     *
     * @param date
     * @param billType
     */
    private String getBillNoKey(Date date, int billType) {
        return DmDateUtil.format2DateStr(date).concat("_").concat(String.valueOf(billType));
    }

    /**
     * 从数据库获取
     *
     * @param billCode
     * @param billCode
     * @param prefix
     * @param key
     * @param date
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String getBillNoFromDb(int billCode, String prefix, boolean isPrefix, String key, Date date) {
        BillNoAllocEntity billNoAllocDb = billNoAllocMapper.query(billCode, date);
        // 如果本机是第一个申请的
        if (Objects.isNull(billNoAllocDb)) {
            BillNoAllocEntity newEntity = new BillNoAllocEntity();
            newEntity.setAllocId(UUID.randomUUID().toString(true));
            newEntity.setBillCode(billCode);
            newEntity.setPrefix(prefix);
            newEntity.setMaxId(step);
            newEntity.setStep(step);
            newEntity.setBussDate(date);
            newEntity.setCreateTime(date);
            newEntity.setUpdateTime(date);
            billNoAllocMapper.insert(newEntity);

            BillNoAlloc billNoAlloc = new BillNoAlloc();
            BeanUtils.copyProperties(newEntity, billNoAlloc);
            billNoAlloc.setStartNo(1);
            billNoAlloc.setEndNo(step);
            billNoCache.setCache(key, billNoAlloc);
            return getAndSetBillNo(key, billNoAlloc, isPrefix);
        } else {
            BillNoAlloc billNoAlloc = new BillNoAlloc();
            BeanUtils.copyProperties(billNoAllocDb, billNoAlloc);
            billNoAlloc.setStartNo(billNoAllocDb.getMaxId() + 1);
            billNoAlloc.setEndNo(billNoAllocDb.getMaxId() + step);

            BillNoAllocEntity newEntity = new BillNoAllocEntity();
            newEntity.setAllocId(billNoAllocDb.getAllocId());
            newEntity.setMaxId(billNoAlloc.getEndNo());
            // 暂时不做动态调整步长
            newEntity.setStep(step);
            billNoAllocMapper.update(newEntity);
            billNoCache.setCache(key, billNoAlloc);
            return getAndSetBillNo(key, billNoAlloc, isPrefix);
        }
    }

    /**
     * 获取No
     *
     * @param key
     * @param billNoAllocCache
     * @return
     */
    private String getAndSetBillNo(String key, BillNoAlloc billNoAllocCache, boolean isPrefix) {
        StringBuilder sb = new StringBuilder();
        if (isPrefix) {
            sb.append(billNoAllocCache.getPrefix());
        }
        // 不足8位的前面补0（足够8位的不再补）
        sb.append(DmDateUtil.format2SimpleDateStr(billNoAllocCache.getBussDate()))
                .append(billNoAllocCache.getBillCode()).append(String.format("%08d", billNoAllocCache.getStartNo()));

        // 如果使用到了最后一个，设置失效
        if (billNoAllocCache.getStartNo().equals(billNoAllocCache.getEndNo())) {
            billNoCache.removeCache(key);
        } else {
            billNoAllocCache.setStartNo(billNoAllocCache.getStartNo() + 1);
            billNoCache.setCache(key, billNoAllocCache);
        }
        return sb.toString();
    }
}
