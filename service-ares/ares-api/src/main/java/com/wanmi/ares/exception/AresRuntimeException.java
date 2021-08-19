package com.wanmi.ares.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 统一异常处理
 * Created by of628-wenzhi on 22/9/2017.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AresRuntimeException extends RuntimeException{

    private static final long serialVersionUID = -1857452010432466121L;

    private String errorCode = "";

    private Object data;

    private String result = "fail";

    private Object[] params;

    /**
     * 默认构造，展示系统异常
     */
    public AresRuntimeException() {
        super();
        this.errorCode="R-000001";
    }

    /**
     * 默认errorCode为 空字符串
     * @param cause
     */
    public AresRuntimeException(Throwable cause){
        this("", cause);
    }

    /**
     * 只有出错信息
     * 多用于系统自身发生的异常，此时没有上级异常
     * @param result 出错信息
     */
    public AresRuntimeException(String result, String errorCode) {
        super();
        this.result = result;
        this.errorCode = errorCode;
    }


    /**
     * 只有出错信息
     * 多用于系统自身发生的异常，此时没有上级异常
     * @param errorCode 异常码 异常码的错误信息会被messageSource读取
     */
    public AresRuntimeException(String errorCode) {
        super();
        this.errorCode = errorCode;
    }

    /**
     * 只有出错信息
     * 多用于系统自身发生的异常，此时没有上级异常
     * @param errorCode
     * @param params
     */
    public AresRuntimeException(String errorCode, Object[] params) {
        super();
        this.errorCode = errorCode;
        this.params = params;
    }

    /**
     * 错误码 + 上级异常
     * @param errorCode
     * @param cause
     */
    public AresRuntimeException(String errorCode, Throwable cause){
        super(cause);
        this.errorCode = errorCode;
    }

    /**
     * 返回数据 + 异常码
     * @param data
     * @param errorCode
     */
    public AresRuntimeException(Object data, String errorCode){
        this.data = data;
        this.errorCode = errorCode;
    }

    /**
     * 返回值 + 异常链
     * @param data
     * @param cause
     */
    public AresRuntimeException(Object data, Throwable cause){
        super(cause);
        this.data = data;
    }
}
