package com.choice.cloud.sysmonitor.core.billno.enums;

import lombok.Getter;

/**
 * 业务类型枚举（需要获取单据号的）
 *
 * @author zhaojufei
 */
@Getter
public enum BillTypeEnum {
    ORDER(101, "ORD","订单号");
    private int code;
    private String prefix;
    private String desc;

    BillTypeEnum(int code, String prefix, String desc) {
        this.code = code;
        this.prefix = prefix;
        this.desc = desc;
    }

    public static BillTypeEnum getEnum(int code) {
        for (BillTypeEnum t : BillTypeEnum.values()) {
            if (t.getCode() == code) {
                return t;
            }
        }

        return null;
    }

}
