package com.wanmi.sbc.liveroom;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
@Component
public  class DateUtils {
    // 将传入时间与当前时间进行对比，是否今天\昨天\前天\同一年
    public   String getTime(Date date) {
        boolean sameYear = false;
        String todySDF = "HH:mm";
        String otherSDF = "yyyy-MM-dd";
        String otherYearSDF = "yyyy-MM-dd";
        SimpleDateFormat sfd = null;
        String time = "";
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        Date now = new Date();
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTime(now);
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayCalendar.set(Calendar.MINUTE, 0);

        if (dateCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)) {
            sameYear = true;
        } else {
            sameYear = false;
        }


        if (dateCalendar.after(todayCalendar)) {// 判断是不是今天
            todayCalendar.add(Calendar.DATE, +1);
            if (dateCalendar.before(todayCalendar)) {
                sfd = new SimpleDateFormat(todySDF);
                time = sfd.format(date);
                return "今天 " + time;
            } else {
                todayCalendar.add(Calendar.DATE, +1);
                if (dateCalendar.before(todayCalendar)) {
                    sfd = new SimpleDateFormat(todySDF);
                    time = sfd.format(date);
                    return "明天 " + time;
                } /*else {
                    todayCalendar.add(Calendar.DATE, +1);
                    if (dateCalendar.before(todayCalendar)) {
                        sfd = new SimpleDateFormat(todySDF);
                        time = sfd.format(date);
                        return "后天 " + time;
                    }
                }*/
            }
        }
        if (sameYear) {
            sfd = new SimpleDateFormat(otherSDF);
            time = sfd.format(date);
        } else {
            sfd = new SimpleDateFormat(otherYearSDF);
            time = sfd.format(date);
        }
        return time;
    }
}