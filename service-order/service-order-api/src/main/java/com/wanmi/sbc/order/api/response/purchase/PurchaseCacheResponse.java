package com.wanmi.sbc.order.api.response.purchase;

import com.wanmi.sbc.marketing.bean.vo.GoodsInfoMarketingVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 前端购物车缓存对象
 */
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseCacheResponse {

    /**
     * 分销员佣金比例
     */
    private BigDecimal commissionRate;

    /**
     * 可用积分
     */
    private Long pointsAvailable;

    /**
     * 单品缓存列表
     */
    private List<GoodsInfoMarketingVO> goodsInfoCacheList = new ArrayList<>();

}
