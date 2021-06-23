package com.wanmi.sbc.erp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: sbc-background
 * @description: ERP商品SKU
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-27 14:04
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ERPGoodsInfo implements Serializable {

    /**
     * 规格ID
     */
    @JsonProperty("id")
    private String id;

    /**
     * 规格代码
     */
    @JsonProperty("code")
    private String code;

    /**
     * 规格名称
     */
    @JsonProperty("name")
    private String name;

    /**
     * 成本价
     */
    @JsonProperty("cost_price")
    private BigDecimal costPrice;
}