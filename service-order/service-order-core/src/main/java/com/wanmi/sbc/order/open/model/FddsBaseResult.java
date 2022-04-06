package com.wanmi.sbc.order.open.model;

import lombok.Data;

import java.io.*;

/**
 * @author Liang Jun
 * @date 2022-04-06 11:48:00
 */
@Data
public class FddsBaseResult<T> implements Serializable {
    /**
     * 状态码
     */
    private String status;
    /**
     * 消息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;


    /**
     * 是否下单成功
     */
    public boolean isSuccess() {
        return "0000".equals(status);
    }

    public static <T> FddsBaseResult<T> success(T content) {
        FddsBaseResult<T> result = new FddsBaseResult();
        result.setData(content);
        result.setStatus("0000");
        return result;
    }
}
