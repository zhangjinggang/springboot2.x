package com.choice.cloud.sysmonitor.common.util;


import com.choice.cloud.sysmonitor.core.bean.BusinessException;
import com.choice.cloud.sysmonitor.core.enums.ResponseCodeEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;


/**
 * 断言工具类
 *
 * @author zhaojufei
 * @since 2019-6-21 16:45
 */
public class DmAssert {

    /**
     * 字符串不为空断言
     * 
     * @param para
     * @param res
     */
    public static void notBlank(String para, ResponseCodeEnum res) {
        if (StringUtils.isBlank(para)) {
            throw new BusinessException(res);
        }
    }

    /**
     * 集合不为空断言
     * 
     * @param collection
     * @param res
     */
    public static void notEmpty(Collection<?> collection, ResponseCodeEnum res) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BusinessException(res);
        }
    }

    /**
     * 不为null断言
     * 
     * @param object
     * @param res
     */
    public static void notNull(Object object,  ResponseCodeEnum res) {
        if (object == null) {
            throw new BusinessException(res);
        }
    }

    /**
     * 判断给定值知否符合给定的正则表达式
     */
    public static void regex(String value, String regex, ResponseCodeEnum res) {
        if (!value.matches(regex)) {
            throw new BusinessException(res);
        }
    }

}
