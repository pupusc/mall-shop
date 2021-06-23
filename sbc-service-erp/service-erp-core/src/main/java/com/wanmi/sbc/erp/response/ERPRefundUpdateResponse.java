package com.wanmi.sbc.erp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @program: sbc-background
 * @description: ERP退款修改退款状态返回报文对象
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-27 18:33
 **/
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPRefundUpdateResponse extends ERPBaseResponse{

    /**
     * 平台单号
     */
    @JsonProperty("platform_code")
    private String platformCode;
}
