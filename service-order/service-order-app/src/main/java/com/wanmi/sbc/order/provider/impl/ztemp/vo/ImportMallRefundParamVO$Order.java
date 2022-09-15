package com.wanmi.sbc.order.provider.impl.ztemp.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ImportMallRefundParamVO$Order {
    /**
     * 主订单ID
     */
    private Long orderId;
    /**
     * 平台退款ID
     */
    @NotNull
    private String platformRefundId;
    /**
     * 执行时间
     */
    @NotNull
    private LocalDateTime applyTime;
    /**
     * 关闭时间
     */
    private LocalDateTime closeTime;
    /**
     * 备注
     */
    private String memo;
}
