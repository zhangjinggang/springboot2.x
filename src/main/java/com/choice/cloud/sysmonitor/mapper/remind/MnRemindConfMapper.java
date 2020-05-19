package com.choice.cloud.sysmonitor.mapper.remind;

import com.choice.cloud.sysmonitor.domain.dto.PageMnRemindConfDto;
import com.choice.cloud.sysmonitor.domain.entity.remind.MnRemindConf;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 邮件提醒配置表(MnRemindConf)表数据库访问层
 *
 * @author ZhaoJuFei
 * @since 2020-05-19 09:16:14
 */
@Mapper
public interface MnRemindConfMapper {

    /**
     * 查询指定行数据
     *
     * @param pageMnRemindConfDto 查询条件
     * @return 对象列表
     */
    List<MnRemindConf> queryAllByLimit(PageMnRemindConfDto pageMnRemindConfDto);

}