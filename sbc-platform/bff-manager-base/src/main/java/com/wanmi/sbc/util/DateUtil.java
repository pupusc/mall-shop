package com.wanmi.sbc.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * <p>日期时间操作工具类</p>
 * Created by of628-wenzhi on 2018-08-29-下午4:44.
 */
public class DateUtil {
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
     *
     * @param timestamp 时间戳
     */
    public static LocalDateTime getLocalDateTimeFromUnixTimestamp(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
    }
}
