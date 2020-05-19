package com.choice.cloud.sysmonitor.core.enums;

/**
 * 返回Code枚举，建议统一在此处定义全局唯一异常码
 * 码的使用除了200代表成功，其他码应该尽量避开HTTP状态码，防止歧义。
 * 建议2XX代表警告，5XX代表错误
 *
 * @author zhaojufei
 */
public enum ResponseCodeEnum {
    /**
     * SUCCESS
     */
    SUCCESS("200", "SUCCESS"),

    /**
     * SUCCESS
     */
    SUCCESS2("10000", "SUCCESS"),

    AUTH_NOT_PASS("520", "非法访问"),
    PARAM_ERROR("521", "参数错误"),
    SYSTEM_ERROR("522", "系统错误"),
    BUSINESS_ERROR("523", "业务异常"),

    NOT_GET_LOCK("524", "操作等待超时，请稍后重试。"),
    NOT_FIND_LOCK_KEY("525", "未找到到分布式锁Key，请求处理失败。"),
    NOT_GET_LOCK_TASK("526", "其他机器正在执行定时任务，本机不再执行"),

    FORM_SUBMIT_FAIL("530", "数据提交失败，请联系相关人员。"),
    FORM_SUBMIT_TOKEN_INVALID("531", "非法操作或Token已失效，请刷新页面后重试"),
    USER_ID_EMPTY("532", "当前用户ID为空"),
    USER_TENANT_ID_EMPTY("533", "当前用户商户ID为空"),
    USER_TOKEN_EMPTY("534", "当前用户TOKEN为空"),
    USER_TOKEN_LIMIT("535", "无效操作过多，有恶意刷token的嫌疑"),

    SEGMENT_TOO_MUCH("555", "版本号不合法：版本号段数过多"),
    DIGIT_TOO_LONG("556", "版本号不合法：版本号位数过多"),

    BILL_TYPE_NOT_EXISTS("561", "业务类型不存在");


    /**
     * 状态码
     */
    private final String code;
    /**
     * 描述信息
     */
    private final String desc;

    ResponseCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
