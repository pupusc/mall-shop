package com.wanmi.ares.utils;

import com.wanmi.ares.enums.QueryDateCycle;
import com.wanmi.ares.report.trade.model.request.TradeCollect;
import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhangjin on 2017/4/28.
 */
public class DateUtil {

    public static final String FMT_MONTH_1 = "yyyyMM";

    public static final String FMT_MONTH_2 = "yyyy-MM";

    public static final String FMT_DATE_1 = "yyyy-MM-dd";

    public static final String FMT_DATE_2 = "yyyyMMdd";

    public static final String FMT_DATE_3 = "yyyy/MM/dd";

    public static final String FMT_TIME_1 = "yyyy-MM-dd HH:mm:ss";

    public static final String FMT_TIME_2 = "yyyy-MM-dd HH:mm";

    public static final String FMT_TIME_3 = "yyyyMMddHHmmss";

    public static final String FMT_TIME_4 = "MM/dd";

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
     * 2017-06-23 -> 2017-06-23 00:00
     *
     * @param time time
     * @return LocalDateTime
     */
    public static LocalDateTime parseDay(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FMT_DATE_1);
        return LocalDate.parse(time, formatter).atStartOfDay();
    }

    public static LocalDate parseLocalDate(String time, String fmt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fmt);
        return LocalDate.parse(time, formatter);
    }


    /**
     * 获取全部当前时间
     *
     * @return
     */
    public static String nowTime() {
        return format(LocalDateTime.now(), FMT_TIME_1);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String now() {
        return format(LocalDateTime.now(), FMT_DATE_1);
    }

    /**
     * 获取年月时间
     *
     * @return
     */
    public static String yearMonth(LocalDate date) {
        return format(date, FMT_MONTH_2);
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

    public static String formatLocalDate(LocalDate localDate, String fmt) {
        return localDate.format(DateTimeFormatter.ofPattern(fmt));
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

    public static String getWeekStr(LocalDate date) {
        String weekStr = null;
        date.getDayOfWeek();
        switch (date.getDayOfWeek()) {
            case MONDAY:
                weekStr = "周一";
                break;
            case TUESDAY:
                weekStr = "周二";
                break;
            case WEDNESDAY:
                weekStr = "周三";
                break;
            case THURSDAY:
                weekStr = "周四";
                break;
            case FRIDAY:
                weekStr = "周五";
                break;
            case SATURDAY:
                weekStr = "周六";
                break;
            case SUNDAY:
                weekStr = "周日";
                break;
        }
        return weekStr;
    }

    public static String[] getEsIndexName(String prefix, LocalDate beginTime, LocalDate endTime) {
        Period p = Period.between(beginTime, endTime);
        int num = p.getYears() * 12 + p.getMonths();
        String[] strs = null;
        if (num > 0) {
            strs = new String[num];
            for (int i = 0; i < num; i++) {
                strs[i] = prefix.concat(beginTime.plusMonths(i + 1).format(DateTimeFormatter.ofPattern("yyyyMM")));
            }
        } else {
            if (!beginTime.getMonth().equals(endTime.getMonth())) {
                strs = new String[2];
                strs[0] = prefix.concat(beginTime.format(DateTimeFormatter.ofPattern("yyyyMM")));
                strs[1] = prefix.concat(endTime.format(DateTimeFormatter.ofPattern("yyyyMM")));
            } else {
                strs = new String[1];
                strs[0] = prefix.concat(beginTime.format(DateTimeFormatter.ofPattern("yyyyMM")));
            }
        }
        return strs;
    }

    public static String[] getEsTypeName(String prefix, LocalDate beginTime, LocalDate endTime) {
        Period p = Period.between(beginTime, endTime);
        int num = (int) ChronoUnit.DAYS.between(beginTime,endTime);
        String[] strs = null;
        if (num > 0) {
            strs = new String[num + 1];
            for (int i = 0; i <= num; i++) {
                strs[i] = prefix.concat(beginTime.plusDays(i).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            }
        } else {
            strs = new String[1];
            strs[0] = prefix.concat(beginTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        }
        return strs;
    }


    public static List<LocalDate> getDateDiff(LocalDate beginTime, LocalDate endTime) {
        int num = (int) ChronoUnit.DAYS.between(beginTime, endTime);
        List<LocalDate> list = new ArrayList<>();
        if (num > 0) {
            for (int i = 0; i <= num; i++) {
                list.add(beginTime.plusDays(i));
            }
        } else {
            list.add(beginTime);
        }
        return list;
    }


    /**
     * 转换类型  LocalDateTime to string
     *
     * @param date date
     * @return LocalDateTime
     */
    public static String format(LocalDate date, String fmt) {
        return date.format(DateTimeFormatter.ofPattern(fmt));
    }

    /**
     * 转换类型  string to LocalDate
     *
     * @param time time
     * @return LocalDateTime
     */
    public static LocalDate parse2Date(String time, String fmt) {
        return LocalDate.parse(time, DateTimeFormatter.ofPattern(fmt));
    }

    /**
     * milliseconds to LocalDate
     *
     * @param milliseconds milliseconds
     * @return LocalDate
     */
    public static LocalDate parse2DateFromMilliseconds(long milliseconds) {
        return Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 根据日期周期转换成具体日期
     *
     * @param cycle QueryDateCycle
     * @return LocalDate 格式：yyyy-MM-dd
     */
    public static LocalDate parseByDateCycle(QueryDateCycle cycle) {
        if (cycle == null) {
            return LocalDate.now();
        }
        switch (cycle) {
            case today:
                return LocalDate.now();
            case yesterday:
                return LocalDate.now().minusDays(1);
            case latest7Days:
                return LocalDate.now().minusDays(7);
            case latest30Days:
                return LocalDate.now().minusDays(30);
            default:
                return LocalDate.now();
        }
    }

    /**
     * 根据年和月转换成该年该月最后一天的日期，如"201710",则返回"2017-10-31"
     *
     * @param yearMonth 年月，格式"yyyy-MM"
     * @return LocalDate 格式：yyyy-MM-dd
     */
    public static LocalDate parseByMonth(String yearMonth) {
        int year = Integer.parseInt(StringUtils.left(yearMonth, 4));
        Month month = Month.of(Integer.parseInt(StringUtils.right(yearMonth, 2)));
        return LocalDate.of(year, month, month.length(Year.isLeap(year)));
    }

    /**
     * 根据年和月转换成该年该月最后一天的日期，如"201710",则返回"2017-10-01"
     *
     * @param yearMonth 年月，格式"yyyy-MM"
     * @return LocalDate 格式：yyyy-MM-dd
     */
    public static LocalDate pareeByMonthFirst(String yearMonth) {
        int year = Integer.parseInt(StringUtils.left(yearMonth, 4));
        Month month = Month.of(Integer.parseInt(StringUtils.right(yearMonth, 2)));
        return LocalDate.of(year, month, 1);
    }

    /**
     * @Author lvzhenwei
     * @Description 获取当前日期的前几个月的第一天和最后一天
     * @Date 14:36 2019/8/22
     * @Param [monthNum, dateType]
     *  monthNum: 上个月为1(前几个月就是对应的正整数)，当前月份为0;
     *  dateType:第一天为：0，最后一天为:1;
     * @return java.time.LocalDate
     **/
    public static LocalDate getMonthFirstAndLastDayDate(int monthNum, int dateType) {
        LocalDate lastMonthDate = LocalDate.now().minusMonths(monthNum);
        int dayNum = lastMonthDate.getDayOfMonth();
        int lengthOfMonth = lastMonthDate.lengthOfMonth();
        if (dateType == 0) {
            return lastMonthDate.minusDays(dayNum-1);
        } else {
            return lastMonthDate.plusDays(lengthOfMonth - dayNum);
        }
    }

    /**
     * @Author lvzhenwei
     * @Description 获取指定日期的前几个月的第一天和最后一天
     * @Date 14:36 2019/8/22
     * @Param [monthNum, date]
     * @return java.time.LocalDate
     **/
    public static WeekDate getMonthFirstAndLastDayDate(int monthNum,LocalDate date) {
        LocalDate lastMonthDate = date.minusMonths(monthNum);
        int dayNum = lastMonthDate.getDayOfMonth();
        int lengthOfMonth = lastMonthDate.lengthOfMonth();
        return WeekDate.builder()
                .beginDate(lastMonthDate.minusDays(dayNum-1))
                .endDate(lastMonthDate.plusDays(lengthOfMonth - dayNum))
                .build();
    }

    /**
     * @Author lvzhenwei
     * @Description 根据传入的开始时间和结束时间计算出对应时间内的所有自然周的日期
     * @Date 17:34 2019/8/22
     * @Param [beginDate, endDate]
     * @return java.util.List<com.wanmi.ares.utils.WeekDate>
     **/
    public static List<WeekDate> getWeekLastDay(LocalDate beginDate, LocalDate endDate){
        List<WeekDate> weekDateList = new ArrayList<>();
        LocalDate weekBeginDate = beginDate;
        LocalDate weekEndDate = getWeekLastDay(beginDate);
        while(true){
            WeekDate weekDate = new WeekDate();
            if(weekBeginDate.isAfter(endDate)){
                break;
            } else if(weekEndDate.isAfter(endDate)){
                weekDate.setBeginDate(weekBeginDate);
                weekDate.setEndDate(endDate);
                weekDateList.add(weekDate);
                break;
            } else {
                weekDate.setBeginDate(weekBeginDate);
                weekDate.setEndDate(weekEndDate);
                weekDateList.add(weekDate);
                weekBeginDate = weekEndDate.plusDays(1);
                weekEndDate = weekBeginDate.plusDays(6);
            }
        }
        return weekDateList;
    }

    /**
     * @Author lvzhenwei
     * @Description 根据传入的开始时间和结束时间计算出对应时间内的所有自然周的日期
     * @Date 17:34 2019/8/22
     * @Param [beginDate, endDate]
     * @return java.util.List<com.wanmi.ares.utils.WeekDate>
     **/
    public static List<TradeCollect> getWeekLastDayTrade(LocalDate beginDate, LocalDate endDate){
        return getWeekLastDay(beginDate, endDate).stream().map(i->
                TradeCollect.builder().beginDate(i.getBeginDate()).endDate(i.getEndDate())
                        .build()).collect(Collectors.toList());
    }

    public static LocalDate getWeekLastDay(LocalDate beginDate){
        DayOfWeek dayOfWeek = beginDate.getDayOfWeek();
        if(dayOfWeek.name().equals("MONDAY")){
            return beginDate.plusDays(6);
        } else if(dayOfWeek.name().equals("TUESDAY")){
            return beginDate.plusDays(5);
        } else if(dayOfWeek.name().equals("WEDNESDAY")){
            return beginDate.plusDays(4);
        } else if(dayOfWeek.name().equals("THURSDAY")){
            return beginDate.plusDays(3);
        } else if(dayOfWeek.name().equals("FRIDAY")){
            return beginDate.plusDays(2);
        } else if(dayOfWeek.name().equals("SATURDAY")){
            return beginDate.plusDays(1);
        } else if(dayOfWeek.name().equals("SUNDAY")){
            return beginDate.plusDays(0);
        } else {
            return null;
        }
    }


    /**
     * 获取当天起始日期
     * @param date
     * @return
     */
    public static TradeCollect computeDateIntervalDay(LocalDate date){
        return TradeCollect.builder()
                .beginDate(date)
                .endDate(date)
                .build();
    };

    /**
     * 获取昨天起始日期
     * @param date
     * @return
     */
    public static TradeCollect computeDateIntervalYesterday(LocalDate date){
        return TradeCollect.builder()
                .beginDate(date.minusDays(1))
                .endDate(date.minusDays(1))
                .build();
    };

    /**
     * 获取最近七天起始日期
     * @param date
     * @return
     */
    public static TradeCollect computeDateIntervalSeven(LocalDate date){
        return TradeCollect.builder()
                .beginDate(date.minusDays(7))
                .endDate(date.minusDays(1))
                .build();
    };

    /**
     * 获取最近三十天起始日期
     * @param date
     * @return
     */
    public static TradeCollect computeDateIntervalThirtyDay(LocalDate date){
        return TradeCollect.builder()
                .beginDate(date.minusDays(30))
                .endDate(date.minusDays(1))
                .build();
    };

    /**
     * 获取最近三十天 周的起始日期
     * @param date
     * @return
     */
    public static List<TradeCollect> computeDateIntervalThirtyDayWeeks(LocalDate date){
        TradeCollect startEndDate = computeDateIntervalThirtyDay(date);
        List<WeekDate> weekLastDay = DateUtil.getWeekLastDay(startEndDate.getBeginDate(), startEndDate.getEndDate());
        return weekLastDay.stream().map(i-> TradeCollect.builder()
                .beginDate(i.getBeginDate())
                .endDate(i.getEndDate()).build()).collect(Collectors.toList());
    };

    /**
     * 获取自然月的的起始日期
     * @param date
     * @return
     */
    public static TradeCollect computeDateIntervalMonthDay(int monNum,LocalDate date){
        WeekDate weekDate = DateUtil.getMonthFirstAndLastDayDate(monNum, date);
        return TradeCollect.builder()
                .beginDate(weekDate.getBeginDate())
                .endDate(weekDate.getEndDate()).build();
    };

    public static LocalDateTime getBeginTime(LocalDate date){

        return LocalDateTime.of(date,LocalTime.MIN);
    }
    public static LocalDateTime getEndTime(LocalDate date){
        return LocalDateTime.of(date,LocalTime.MAX);
    }
    public static String getBeginTime(LocalDate date,String format){
       return getBeginTime(date).format(DateTimeFormatter.ofPattern(format));
    }

    public static String getEndTime(LocalDate date,String format){
        return getEndTime(date).format(DateTimeFormatter.ofPattern(format));
    }

    public static void main(String[] args){
        System.out.println(getBeginTime(LocalDate.now(),FMT_TIME_1));
        System.out.println(getEndTime(LocalDate.now(),FMT_TIME_1));
    }
}
