package com.choice.cloud.sysmonitor.domain.dto;

import lombok.Data;

/**
 * 分页参数父类
 */
@Data
public class PageDto {
    /**
     * 当前页
     */
    private Integer current = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 开始索引，limit用法使用
     */
    private Integer start = 0;

    public void calStart() {
        this.start = (current - 1) * pageSize;
    }

    public void setCurrent(Integer current){
        this.current = current;
        this.calStart();
    }
}
