package com.wanmi.ares.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @ClassName WeekDate
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/8/22 17:31
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeekDate {

    /**
     * 开始时候
     * 20170925
     */
    private LocalDate beginDate;

    /**
     * 结束时间
     * 20170926
     */
    private LocalDate endDate;
}
