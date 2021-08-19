package com.wanmi.sbc.marketing.bean.vo;

import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品关联的营销信息
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfoMarketingVO {

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 单品id
     */
    private String goodsInfoId;

    /**
     * 分销商品状态
     */
    private DistributionGoodsAudit distributionGoodsAudit;

    /**
     * 商品关联的营销活动列表
     */
    private List<MarketingViewVO> marketingViewList = new ArrayList<>();

}
