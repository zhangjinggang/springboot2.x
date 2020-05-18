package com.choice.cloud.sysmonitor.config.log;

/**
 * @author Z.Yang
 * @date 2020/2/25 22:01
 */
public enum LogOrigin {
    HTTP_IN("HTTP-IN"),

    MQ_IN("MQ-IN"),

    OUT("OUT"),

    DB("DB"),

    ONS_OUT("ONS-OUT");

    private String value;

    private LogOrigin(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
