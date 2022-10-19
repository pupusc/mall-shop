package com.wanmi.sbc.erp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sbc-background
 * @description: 订单支付明细
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-26 16:58
 **/
@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ERPTradePayment implements Serializable {

    private static final long serialVersionUID = -4632963488331334596L;
    /**
     * 支付方式
     */
    @JsonProperty("pay_type_code")
    private String payTypeCode;

    /**
     * 支付金额
     */
    @JsonProperty("payment")
    private String payment;

    /**
     * 交易号
     */
    @JsonProperty("pay_code")
    private String payCode;

    /**
     * 账号
     */
    @JsonProperty("account")
    private String account;

    /**
     * 支付时间
     */
    @JsonProperty("paytime")
    private long paytime;


}
