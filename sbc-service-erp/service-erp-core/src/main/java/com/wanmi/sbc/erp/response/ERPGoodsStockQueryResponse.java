package com.wanmi.sbc.erp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanmi.sbc.erp.entity.ERPGoodsInfoStock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @program: sbc-background
 * @description: 商品库存查询接口
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-27 18:20
 **/
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPGoodsStockQueryResponse extends ERPBaseResponse {

    /**
     * 库存记录列表
     */
    @JsonProperty("stocks")
    private List<ERPGoodsInfoStock> stocks;

    /**
     * 总记录数
     */
    @JsonProperty("total")
    private int total;
}
