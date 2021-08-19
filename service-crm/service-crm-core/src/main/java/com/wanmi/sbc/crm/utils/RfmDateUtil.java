package com.wanmi.sbc.crm.utils;

import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.crm.bean.enums.Period;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-10-15
 * \* Time: 13:37
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class RfmDateUtil {
    public static String getDayTime(Period period){

        LocalDateTime time = LocalDateTime.of(LocalDate.now(),LocalTime.MIN);
        switch (period){
            case ONE_MONTH:
                time = time.minusMonths(1);
                break;
            case THREE_MONTH:
                time = time.minusMonths(3);
                break;
            case SIX_MONTH:
                time = time.minusMonths(6);
                break;
            case ONE_YEAR:
                time = time.minusYears(1);

        }
        return DateUtil.format(time,DateUtil.FMT_TIME_1);
    }

    public static String getMinusDayTime(int days){
        LocalDateTime time = LocalDateTime.of(LocalDate.now(),LocalTime.MIN);
        time = time.minusDays(days);
        return DateUtil.format(time,DateUtil.FMT_TIME_1);
    }

    /**
     * 时间补充
     * @return
     */
    public static List<String> fillTimeRange(List<String> times) {
        final String fmt = "%s-%s";
        final String beginTime = "00:00";
        final String endTime = "24:00";
        List<Map<String, String>> list = times.stream().map(t -> {
            String[] range = t.split("-");
            Map<String, String> time = new HashMap<>();
            time.put("begin", range[0].length() > 1 ? range[0] : "0".concat(range[0]));
            time.put("end", range[1].length() > 1 ? range[1] : "0".concat(range[1]));
            return time;
        }).sorted(Comparator.comparing(t -> t.get("begin"))).collect(Collectors.toList());

        List<String> newList = new ArrayList<>();
        int len = list.size();
        for (int i = 0; i < len; i++) {
            Map<String, String> timeRage = list.get(i);
            if (i == 0 && (!beginTime.equals(timeRage.get("begin")))) {
                //补充00:00-开始时间
                newList.add(String.format(fmt, beginTime, timeRage.get("begin")));
            } else if (i > 0 && i < len - 1) {
                //补充中间时间差
                Map<String, String> lastTimeRage = list.get(i - 1);
                if (!lastTimeRage.get("end").equals(timeRage.get("begin"))) {
                    newList.add(String.format(fmt, lastTimeRage.get("end"), timeRage.get("begin")));
                }
            }
            newList.add(String.format(fmt, timeRage.get("begin"), timeRage.get("end")));

            //补充结束时间-24:00
            if (i == len - 1 && !endTime.equals(timeRage.get("end"))) {
                newList.add(String.format(fmt, timeRage.get("end"), endTime));
            }
        }
        return newList;
    }
}
