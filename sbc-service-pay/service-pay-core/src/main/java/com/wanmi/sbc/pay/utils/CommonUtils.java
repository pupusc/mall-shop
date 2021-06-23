package com.wanmi.sbc.pay.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * <p>模块工具类</p>
 * Created by of628-wenzhi on 2017-08-10-下午4:24.
 */
public class CommonUtils {

    /**
     * 毫秒数转LocalDateTime
     *
     * @param millisecond 毫秒数
     */
    public static LocalDateTime getLocalDateTimeFromMilli(long millisecond) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millisecond), ZoneId.systemDefault());
    }

    /**
     * Unix 时间戳转LocalDateTime
     * @param timestamp 时间戳
     */
    public static LocalDateTime getLocalDateTimeFromUnixTimestamp(long timestamp){
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
    }

}
