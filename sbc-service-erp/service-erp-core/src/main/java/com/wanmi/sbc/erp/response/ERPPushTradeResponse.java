package com.wanmi.sbc.erp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @program: sbc-background
 * @description: 订单推送返回对象
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-26 17:39
 **/
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPPushTradeResponse extends ERPBaseResponse{

    /**
     * 订单ID
     */
    @JsonProperty("id")
    private String id;
}
