package com.wanmi.sbc.erp.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: sbc-background
 * @description: 退货单列表查询接口
 * @author: 0F3685-wugongjiang
 * @create: 2021-02-05 14:23
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class ERPReturnTradeQueryRequest extends ERPBaseRequest{

    /**
     * 商品订单号
     */
    @JsonProperty("platform_code")
    private String platformCode;

    /**
     * 入库状态(0:未入库
     * 1:已入库)
     */
    @JsonProperty("receive")
    private int receive;
}
