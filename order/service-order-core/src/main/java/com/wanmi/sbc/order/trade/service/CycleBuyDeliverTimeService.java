package com.wanmi.sbc.order.trade.service;

import com.wanmi.sbc.goods.bean.enums.DailyIssueType;
import com.wanmi.sbc.goods.bean.enums.DeliveryCycle;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class CycleBuyDeliverTimeService {

    private static final Integer SIX = 6;
    private static final Integer FIVE = 5;

    /**
     * 计算下一次发货时间
     * @return
     */
    public LocalDate getLatestDeliverTime(LocalDate date, DeliveryCycle deliveryCycle, String rule){
        if(StringUtils.isNotBlank(rule)) {
            switch (deliveryCycle) {
                case EVERYDAY:
                    return everyDay(date, rule);
                case WEEKLY:
                    return week(date, rule);
                case MONTHLY:
                    return month(date, rule);
                default:
            }
        }
        return null;
    }

    /**
     * 每日一期
     * @param rule
     * @return
     */
    private LocalDate everyDay(LocalDate date, String rule){
        DailyIssueType dailyIssueType = DailyIssueType.fromValue(Integer.parseInt(rule));
        switch (dailyIssueType) {
            case EVERY_DAY:
                return date.plusDays(NumberUtils.INTEGER_ONE);
            case WORKDAY:
                if(date.getDayOfWeek().ordinal() >= DayOfWeek.FRIDAY.ordinal()) {
                    //周五到周日
                    return date.plusDays(DayOfWeek.values().length - date.getDayOfWeek().ordinal());
                } else {
                    //周一到周四往后推一天
                    return date.plusDays(NumberUtils.INTEGER_ONE);
                }
            case WEEKEND:
                if(date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                    //周六往后推6天
                    return date.plusDays(SIX);
                } else if(date.getDayOfWeek().ordinal() < DayOfWeek.FRIDAY.ordinal()) {
                    //周一到周四
                    return date.plusDays(FIVE - date.getDayOfWeek().ordinal());
                } else {
                    //周五、周六往后推一天
                    return date.plusDays(NumberUtils.INTEGER_ONE);
                }
            default:
        }
        return null;
    }

    /**
     * 每周一期
     * 将指定时间代表的周几和发货日期代表的周几进行比较（按周一到周日的顺序）
     * @param rule
     * @return
     */
    private LocalDate week(LocalDate date, String rule) {
        int nowOrdinal = date.getDayOfWeek().ordinal();
        //发货时间
        DayOfWeek deliverRule = DayOfWeek.of(Integer.parseInt(rule));

        if(deliverRule.equals(date.getDayOfWeek())) {
            //指定时间和发货时间是同一天
            return date.plusDays(DayOfWeek.values().length);
        } else if(deliverRule.ordinal() < nowOrdinal){
            //发货时间在指定时间的前面
            return date.plusDays(DayOfWeek.values().length - date.getDayOfWeek().ordinal());
        } else {
            //发货时间在指定时间的后面
            int days = deliverRule.ordinal() - nowOrdinal;
            return date.plusDays(days);
        }
    }

    /**
     * 每月一期
     * @param rule
     * @return
     */
    private LocalDate month(LocalDate date, String rule){
        //发货时间
        int deliverRule = Integer.parseInt(rule);

        if(deliverRule <= date.getDayOfMonth()){
            int days = date.getDayOfMonth() - deliverRule;
            return date.plusMonths(NumberUtils.INTEGER_ONE).minusDays(days);
        } else {
            int days = deliverRule - date.getDayOfMonth();
            return date.plusDays(days);
        }

    }
}
