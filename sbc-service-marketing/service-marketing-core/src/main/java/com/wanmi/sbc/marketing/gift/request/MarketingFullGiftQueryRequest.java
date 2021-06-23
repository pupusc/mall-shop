package com.wanmi.sbc.marketing.gift.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 营销满赠规则
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketingFullGiftQueryRequest {

    private String customerId;

    @NotNull
    private Long marketingId;

}
