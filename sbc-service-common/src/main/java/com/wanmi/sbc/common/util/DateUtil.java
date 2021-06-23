package com.wanmi.sbc.common.util;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by zhangjin on 2017/4/28.
 */
public class DateUtil {

    public static final String FMT_DATE_1 = "yyyy-MM-dd";

    public static final String FMT_TIME_1 = "yyyy-MM-dd HH:mm:ss";

    public static final String FMT_TIME_2 = "yyyy-MM-dd HH:mm";

    public static final String FMT_TIME_3 = "yyyyMMddHHmmss";

    public static final String FMT_DATE_3 = "MMddHH";

    public static final String FMT_TIME_4 = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final String FMT_TIME_5 = "yyyyMMdd";

    public static final String FMT_TIME_6 = "yyyy-MM-dd HH";

    public static final String FMT_TIME_7 = "yyyyMMddHHmmssSSS";

    public static final String FMT_TIME_8 = "yyyyMMdd HH:mm:ss";

    public static final String QUARTER = "quarter";

    public static final String MONTH = "month";

    public static final String YEAR = "year";

    public final static int[] digits = { 0,1,2,3,4,5,6,7,8,9,10 };

    /**
     * 转换类型 string to LocalDateTime
     *
     * @param time time
     * @return LocalDateTime
     */
    public static LocalDateTime parseDate(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FMT_TIME_2);
        return LocalDateTime.of(LocalDate.parse(time, formatter), LocalTime.MIN);
    }

    /**
     * 转换类型 string to LocalDateTime
     *
     * @param time time
     * @return LocalDateTime
     */
    public static String getDate(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FMT_DATE_1);
        return formatter.format(time);
    }

    /**
     * 转换类型 string to LocalDateTime
     * 2017-06-23 -> 2017-06-23 00:00
     *
     * @param time time
     * @return LocalDateTime
     */
    public static LocalDateTime parseDay(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FMT_DATE_1);
        return LocalDate.parse(time, formatter).atStartOfDay();
    }


    /**
     * 获取全部当前时间
     * @return
     */
    public static String nowTime(){
        return format(LocalDateTime.now(), FMT_TIME_1);
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String nowDate(){
        return format(LocalDateTime.now(),FMT_DATE_1);
    }

    /**
     * 获取当前时间 到小时
     * @return
     */
    public static String nowHourTime(){
        return format(LocalDateTime.now(),FMT_TIME_6);
    }

    /**
     * 获取昨天时间
     * @return
     */
    public static String yesterdayDate(){
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,-1);

        return format(cal.getTime(),FMT_DATE_1);
    }

    /**
     * 获取明天时间
     * @return
     */
    public static String tomorrowDate(){
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,1);

        return format(cal.getTime(),FMT_DATE_1);
    }


    /**
     * 转换类型  LocalDateTime to string
     *
     * @param time time
     * @return LocalDateTime
     */
    public static String format(LocalDateTime time, String fmt) {
        return time.format(DateTimeFormatter.ofPattern(fmt));
    }

    /**
     * 转换类型  Date to string
     *
     * @param time time
     * @return LocalDateTime
     */
    public static String format(Date time, String fmt) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fmt);
        return simpleDateFormat.format(time);
    }

    /**
     * 转换类型  string to LocalDateTime
     *
     * @param time time
     * @return LocalDateTime
     */
    public static LocalDateTime parse(String time, String fmt) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(fmt));
    }

    /**
     * 时间类型转换为LocalDateTime, 针对*request中带有canEmpty注解的时间属性
     * 如果没有canEmpty注解, 需要在外层判断为空则不调用该方法
     * 使用时需要位于copyProperties上方
     * 场景: 修改信息,
     *   有值:
     *     1.前端如果修改值, 传递到后端则为yyyy-MM-dd类型, 长度为10
     *     2.如果不做修改则为yyyy-MM-dd HH:mm:ss.SSS类型
     *   无值:
     *     保存为null
     * @param time 时间
     * @return
     */
    public static LocalDateTime parseDayCanEmpty(String time) {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern(FMT_DATE_1);
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern(FMT_TIME_4);
        if(StringUtils.isNotBlank(time)) {
            if(time.length() == 10) {
                return LocalDateTime.of(LocalDate.parse(time, formatter1), LocalTime.MIN);
            } else {
                return LocalDateTime.parse(time, formatter2);
            }
        }
        return null;
    }

    /**
     * 相差天数
     * @param oldTime
     * @param time
     * @return
     */
    public static Long differentDays(LocalDateTime oldTime,LocalDateTime time) {
        return time.toLocalDate().toEpochDay()-oldTime.toLocalDate().toEpochDay();
    }

    /**
     * 获取某季度的开始日期
     * 季度一年四季， 第一季度：1月-3月， 第二季度：4月-6月， 第三季度：7月-9月， 第四季度：10月-12月
     *
     * @param offset 0本季度，1下个季度，-1上个季度，依次类推
     * @return
     */
    public static LocalDateTime quarterStart(int offset) {
        final LocalDateTime date = LocalDateTime.now().plusMonths(offset * 3);
        int month = date.getMonth().getValue();
        int start = 0;
        if (month >= 1 && month <= 3) {
            start = 1;
        } else if (month >= 4 && month <= 6) {
            start = 4;
        } else if (month >= 7 && month <= 9) {
            start = 7;
        } else if ((month >= 10 && month <= 12)) {
            start = 10;
        }
        return date.plusMonths(start - month).with(TemporalAdjusters.firstDayOfMonth()).minusDays(1);
    }

    /**
     * 明天
     * @return
     */
    public static LocalDate tomorrowDay(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FMT_DATE_1);
        return LocalDate.parse(tomorrowDate(), formatter);
    }

    /**
     * 当前周的第一天
     * @return
     */
    public static LocalDate firstDayOfWeek(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FMT_DATE_1);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date date = cal.getTime();
        return LocalDate.parse(simpleDateFormat.format(date));
    }

    /**
     * 下周的第一天
     * @return
     */
    public static LocalDate firstDayOfNextWeek() {
        return LocalDate.now().with(TemporalAdjusters.next( DayOfWeek.MONDAY));
    }

    /**
     * 当月的第一天
     * @return
     */
    public static LocalDate firstDayOfMonth() {
        return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 下个月的第一天
     * @return
     */
    public static LocalDate firstDayOfNextMonth() {
        return LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
    }

    /**
     * 当年的第一天
     * @return
     */
    public static LocalDate firstDayOfYear() {
        return LocalDate.now().with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 明年的第一天
     * @return
     */
    public static LocalDate firstDayOfNextYear() {
        return LocalDate.now().with(TemporalAdjusters.firstDayOfNextYear());
    }

    /**
     * 根据当前时间转换出Map{month=当前月, quarter=当前季度, year=当前年份}
     * @return
     */
    public static Map<String, Integer> covertToMonthAndQuarterAndYear(LocalDateTime date) {
        int month = date.getMonth().getValue();
        int quarter;
        if (month >= digits[1] && month <= digits[3]) {
            quarter = digits[1];
        } else if (month >= digits[4] && month <= digits[6]) {
            quarter = digits[2];
        } else if (month >= digits[7] && month <= digits[9]) {
            quarter = digits[3];
        } else {
            quarter = digits[4];
        }
        return ImmutableMap.of(MONTH,month,QUARTER,quarter,YEAR,date.getYear());
    }
}
