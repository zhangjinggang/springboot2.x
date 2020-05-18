package com.choice.cloud.sysmonitor.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 公共计算类
 *
 * @author zhaojufei
 */
public class DmCalUtil {

    public static void main(String[] args) {

    }

    /**
     * 四舍五入除法
     *
     * @param num1
     * @param num2
     * @param scale
     * @return
     */
    public static BigDecimal percentDivide(int num1, int num2, int scale) {
        return (new BigDecimal(num1).multiply(new BigDecimal(100))).divide(new BigDecimal(num2), scale,
                RoundingMode.HALF_UP);
    }


    /**
     * 两个数相除，保留小数点
     *
     * @param num1
     * @param num2
     * @param scale
     * @return
     */
    public static BigDecimal percentDivide(BigDecimal num1, BigDecimal num2, int scale) {
        return num1.divide(num2, scale, RoundingMode.HALF_UP);
    }

}
