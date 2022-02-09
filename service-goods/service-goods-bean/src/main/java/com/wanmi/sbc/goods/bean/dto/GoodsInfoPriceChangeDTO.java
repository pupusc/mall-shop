package com.wanmi.sbc.goods.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfoPriceChangeDTO {
    private String goodsId;
    private String goodsInfoId;
    private String goodsName;
    private String changeTime;
    private BigDecimal originalPrice;
    private BigDecimal price;
}
