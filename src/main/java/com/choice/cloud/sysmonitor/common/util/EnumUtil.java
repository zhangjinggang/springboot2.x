package com.choice.cloud.sysmonitor.common.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;

/**
 * @description 枚举工具类补充
 */
public enum EnumUtil {

    /**
     * 单例
     */
    INSTANCE;
    /**
     * 是否属于枚举值
     */
    public static Boolean isContainEnum(Class<? extends Enum<?>> aClass,String s){
        Enum<?>[] enumConstants = aClass.getEnumConstants ( );
        if ( enumConstants == null || StrUtil.isBlank ( s ) ){
            Assert.notNull ( "enum or string not be allowed null" );
            assert enumConstants != null;
        }
        for (Enum<?> anEnum : enumConstants) {
            if ( StrUtil.equals ( anEnum.name (),s,true ) ){
                return true;
            }
        }
        return false;
    }
}
