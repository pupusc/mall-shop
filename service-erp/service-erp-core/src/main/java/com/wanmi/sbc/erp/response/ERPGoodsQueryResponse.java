package com.wanmi.sbc.erp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanmi.sbc.erp.entity.ERPGoods;
import com.wanmi.sbc.erp.entity.ERPGoodsInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @program: sbc-background
 * @description: 管易云ERP商品查询响应对象
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-27 09:56
 **/
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPGoodsQueryResponse extends ERPBaseResponse{

    /**
     * 商品列表
     */
    @JsonProperty("items")
    private List<ERPGoods> items;

    /**
     * 总记录数
     */
    @JsonProperty("total")
    private int total;
}
