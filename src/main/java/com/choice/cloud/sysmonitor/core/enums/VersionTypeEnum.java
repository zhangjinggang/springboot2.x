package com.choice.cloud.sysmonitor.core.enums;

import com.choice.cloud.sysmonitor.core.constant.CommonConstant;
import lombok.Getter;

/**
 * 版本号枚举类
 *
 * @author zhaojufei
 */
@Getter
public enum VersionTypeEnum {

    /**
     * 公司应用、操作系统版本号常量不可随意修改，如果要修改，需要处理历史数据：
     * 程序将版本号转为数字进行版本大小比较，如果修改版本号的组成规则不修改历史数据，
     * 将会造成版本比较错误，进而出现非常严重的后果。
     */
    APP_VERSION("app", 4, 3, "%03d", CommonConstant.RegExp.APP_VERSION),
    OS_VERSION("os", 3, 5, "%05d",
            CommonConstant.RegExp.OS_VERSION);

    private String code;
    private int segment;
    private int numbers;
    private String format;
    private String regexp;

    VersionTypeEnum(String code, int segment, int numbers, String format, String regexp) {
        this.code = code;
        this.segment = segment;
        this.numbers = numbers;
        this.format = format;
        this.regexp = regexp;
    }

    public static VersionTypeEnum getEnum(String code) {
        for (VersionTypeEnum t : VersionTypeEnum.values()) {
            if (t.getCode().equals(code)) {
                return t;
            }
        }
        return null;
    }

}
