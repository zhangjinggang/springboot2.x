package com.choice.cloud.sysmonitor.core.billno.mapper;

import com.choice.cloud.sysmonitor.core.billno.entity.BillNoAllocEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 设备管理-业务单号获取表(DmBillnoAlloc)表数据库访问层
 *
 * @author makejava
 * @since 2019-12-18 14:24:38
 */
@Mapper
public interface BillNoAllocMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param billCode 业务类型
     * @param bussDate 日期
     * @return 实例对象
     */
    BillNoAllocEntity query(@Param("billCode") int billCode, @Param("bussDate") Date bussDate);

    /**
     * 新增数据
     *
     * @param billNoAlloc 实例对象
     * @return 影响行数
     */
    int insert(BillNoAllocEntity billNoAlloc);

    /**
     * 修改数据
     *
     * @param billNoAlloc 实例对象
     * @return 影响行数
     */
    int update(BillNoAllocEntity billNoAlloc);

}
