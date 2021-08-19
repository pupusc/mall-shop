package com.wanmi.ares.report.goods.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品日报表信息
 * Created by daiyitian on 2017/9/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsTotalRatioReport  {

    /**
     * SKU独立访客的下单笔数
     */
    private Long customerCount = 0L;


    private Long totalUv = 0L;

    /**
     * 商品详情转换率
     */
    private BigDecimal ratio = BigDecimal.ZERO;

}
