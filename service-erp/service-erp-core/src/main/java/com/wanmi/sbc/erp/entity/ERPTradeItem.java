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
 * @description: 管易云ERP订单商品集合
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-26 10:42
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ERPTradeItem implements Serializable {

    private static final long serialVersionUID = 4006056439044527818L;
    /**
     * 商品代码
     */
    @JsonProperty("item_code")
    private String itemCode;

    /**
     * 规格代码
     */
    @JsonProperty("sku_code")
    private String skuCode;

    /**
     * 标准单价
     */
    @JsonProperty("origin_price")
    private String originPrice;

    /**
     * 实际单价
     */
    @JsonProperty("price")
    private String price;

    /**
     *商品数量
     */
    @JsonProperty("qty")
    private Integer qty;

    /**
     * 退款状态(0:未退款
     * 1:退款完成
     * 2:退款中)
     */
    @JsonProperty("refund")
    private Integer refund;

    /**
     * 备注
     */
    @JsonProperty("note")
    private String note;

    /**
     * 子订单ID
     */
    @JsonProperty("oid")
    private String oid;

    /**
     * 预计发货日期
     */
    @JsonProperty("plan_delivery_date")
    private String planDeliveryDate;

    /**
     * 是否为预售
     */
    @JsonProperty("presale")
    private boolean presale;


}
