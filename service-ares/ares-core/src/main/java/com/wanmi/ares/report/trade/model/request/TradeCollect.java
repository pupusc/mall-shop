package com.wanmi.ares.report.trade.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * 交易报表查询
 * Created by sunkun on 2017/9/26.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeCollect{

    /**
     * 开始时候
     */
    @NotBlank
    private LocalDate beginDate;

    /**
     * 结束时间
     */
    @NotBlank
    private LocalDate endDate;

}
