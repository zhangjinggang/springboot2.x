package com.choice.cloud.sysmonitor.core.bean;

import com.choice.cloud.sysmonitor.core.enums.ResponseCodeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;


@Data
@NoArgsConstructor
@ToString
public class ResponseData<T> implements Serializable {
    private static final long serialVersionUID = 6556009690114121528L;
    private String code;
    private String msg;
    private T data;

    public ResponseData(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseData(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    /**
     * 通过ResponseCodeEnum构造接口返回体，推荐使用
     */
    public static ResponseData getResponseData(ResponseCodeEnum res) {
        ResponseData body = new ResponseData();
        body.setCode(res.getCode());
        body.setMsg(res.getDesc());
        return body;
    }

    /**
     * 通过ResponseCodeEnum构造接口返回体，推荐使用
     */
    public static ResponseData getResponseData(ResponseCodeEnum res, Object data) {
        ResponseData body = getResponseData(res);
        body.setData(data);
        return body;
    }

    /**
     * 方法名与两个参数的getResponseData区分开来，防止程序调用错。（否则不同jdk版本的选择可能不同）
     */
    public static ResponseData getResponseDataByMsg(ResponseCodeEnum res, String msg) {
        ResponseData body = getResponseData(res);
        body.setMsg(msg);
        return body;
    }

    /**
     * 通过ResponseCodeEnum构造接口返回体，推荐使用
     */
    public static ResponseData getResponseData(ResponseCodeEnum res, String msg, Object data) {
        ResponseData body = getResponseData(res);
        body.setMsg(msg);
        body.setData(data);
        return body;
    }

    /**
     * 完全自定义
     */
    public static ResponseData getResponseData(String code, String msg, Object data) {
        ResponseData body = new ResponseData();
        body.setCode(code);
        body.setMsg(msg);
        body.setData(data);
        return body;
    }

    /**
     * 返回成功
     */
    public static ResponseData createSuccessResData() {
        ResponseData body = getResponseData(ResponseCodeEnum.SUCCESS);
        return body;
    }

    /**
     * 返回成功
     */
    public static ResponseData createSuccessResData(Object data) {
        ResponseData body = getResponseData(ResponseCodeEnum.SUCCESS);
        body.setData(data);
        return body;
    }

    public static ResponseData createBySuccess() {
        return new ResponseData(ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.getDesc());
    }

    /**
     * 是否成功
     *
     * @return
     */
    @JsonIgnore
    public boolean isSuccess() {
        return this.code.equals(ResponseCodeEnum.SUCCESS.getCode()) || this.code.equals(ResponseCodeEnum.SUCCESS2.getCode());
    }
}