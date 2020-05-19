package com.choice.cloud.sysmonitor.core.constant;

/**
 * @author zhaojufei
 */
public class CommonConstant {
    /**
     * 所有URI第一段
     */
    public static final String CONTEXT_PATH = "/sys-monitor";

    /**
     * 用户信息
     */
    public static final String USER = "USER";

    /**
     * 正则表达式
     */
    public static class RegExp {
        /**
         * 公司应用、操作系统版本号常量不可随意修改，如果要修改，需要处理历史数据：
         * 程序将版本号转为数字进行版本大小比较，如果修改版本号的组成规则不修改历史数据，
         * 将会造成版本比较错误，进而出现非常严重的后果。如果要修改,请修改VersionTypeEnum和本类.
         */
        public static final String APP_VERSION = "([0-9]{1,3})+(\\.+([0-9]{1,3})){0,3}";
        public static final String APP_VERSION_MSG = "应用版本号最多4段，每段最多3个数字";

        public static final String OS_VERSION = "([0-9]{1,5})+(\\.+([0-9]{1,5})){0,2}";
        public static final String OS_VERSION_MSG = "操作系统版本号最多3段，每段最多5个数字";
    }

}
