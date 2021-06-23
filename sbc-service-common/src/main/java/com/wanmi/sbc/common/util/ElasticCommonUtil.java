package com.wanmi.sbc.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

/**
 * @Author: songhanlin
 * @Date: Created In 16:08 2020/12/15
 * @Description: Elastic工具类
 */
public final class ElasticCommonUtil {

    /**
     * 模糊查询匹配, 组装查询条件
     *
     * @param keyWord
     * @return
     */
    public static String replaceEsLikeWildcard(String keyWord) {
        return StringUtil.ES_LIKE_CHAR.concat(XssUtils.replaceEsLikeWildcard(keyWord.trim())).concat(StringUtil.ES_LIKE_CHAR);
    }

    /**
     * 格式化时间类型
     * yyyy-MM-dd HH:mm:ss.SSS
     *
     * @param localDate
     * @return
     */
    public static String localDateFormat(String localDate) {
        return DateUtil.format(Objects.requireNonNull(DateUtil.parseDayCanEmpty(localDate)), DateUtil.FMT_TIME_4);
    }

    /**
     * 格式化并且天数+1
     * 时间类型 yyyy-MM-dd HH:mm:ss.SSS
     *
     * @param localDateStr
     * @return
     */
    public static String plusOneDayLocalDateFormat(String localDateStr) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = LocalDate.parse(localDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime beginDateTime = localDate.atTime(LocalTime.MIN).plusDays(1);
        return DateUtil.format(Date.from(beginDateTime.atZone(zoneId).toInstant()), DateUtil.FMT_TIME_4);
    }

}
