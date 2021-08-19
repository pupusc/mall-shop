package com.wanmi.ares.report.goods.model.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品报表公共信息
 * Created by daiyitian on 2017/9/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsTotalResponse {

    /**
     * 主键
     */
    private String id;

    /**
     * 商品总数（SKU）
     */
    private Long total = 0L;

    /**
     * 上架商品数
     */
    private Long addedTotal = 0L;

    /**
     * 商品详情页转化率
     */
    private BigDecimal orderConversion = BigDecimal.ZERO;

    /**
     * 时间
     */
    private String date;

    /**
     * 商户ID
     */
    private String companyId;

    /**
     * 已审核商品数
     */
    private Long checkedTotal = 0L;

    /**
     * 销售中商品数
     */
    private Long saleTotal = 0L;
}
