package com.wanmi.sbc.marketing.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 销售业绩使用的时间判断条件
 */
@Data
@ApiModel
public class QueryConditionTime {

    /**
     * 昨日开始时间
     */
    @ApiModelProperty(value = "昨日开始时间")
    private LocalDateTime yestodayTime;

    /**
     * 今日开始时间
     */
    @ApiModelProperty(value = "今日开始时间")
    private LocalDateTime todayBeginTime;

    /**
     * 本月开始时间
     */
    @ApiModelProperty(value = "本月开始时间")
    private LocalDateTime thisMonthTime;

    public LocalDateTime getYestodayTime() {

        if(yestodayTime != null){
            return yestodayTime;
        }
        LocalDateTime dateTemp = LocalDateTime.now().minusDays(Long.valueOf("1"));
        return LocalDateTime.of(dateTemp.getYear(),dateTemp.getMonth(),dateTemp.getDayOfMonth(),0,0);
    }

    public LocalDateTime getThisMonthTime() {

        if(thisMonthTime != null){
            return thisMonthTime;
        }
        LocalDateTime dateTemp = LocalDateTime.now();
        return LocalDateTime.of(dateTemp.getYear(),dateTemp.getMonth(),1,0,0);
    }

    public LocalDateTime getTodayBeginTime() {

        if(todayBeginTime != null){
            return todayBeginTime;
        }
        LocalDateTime dateTemp = LocalDateTime.now();
        return LocalDateTime.of(dateTemp.getYear(),dateTemp.getMonth(),dateTemp.getDayOfMonth(),0,0);
    }

    public LocalDateTime getYesterdayEndTime() {
        LocalDateTime dateTemp = LocalDateTime.now().minusDays(Long.valueOf("1"));
        return LocalDateTime.of(dateTemp.getYear(),dateTemp.getMonth(),dateTemp.getDayOfMonth(),23,59,59);
    }

}
