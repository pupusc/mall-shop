package com.wanmi.sbc.customer.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaidCardRedisDTO {

    private String customerId;

    private String ruleId;

    private String businessId;

    /**
     * 0 微信 1 支付宝
     */
    private Integer payType;
}
