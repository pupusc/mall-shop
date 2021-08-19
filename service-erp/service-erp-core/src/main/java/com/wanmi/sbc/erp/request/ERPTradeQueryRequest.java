package com.wanmi.sbc.erp.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class ERPTradeQueryRequest extends ERPBaseRequest{

    /**
     * 商城订单号
     */
    @JsonProperty("platform_code")
    private String platformCode;
}
