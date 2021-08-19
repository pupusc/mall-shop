package com.wanmi.sbc.marketing.api.response.market;

import com.wanmi.sbc.marketing.bean.vo.GoodsInfoMarketingVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketInfoForPurchaseResponse {

    /**
     * 分销员佣金比例
     */
    private BigDecimal commissionRate = BigDecimal.ZERO;

    /**
     * 商品营销相关信息
     */
    private List<GoodsInfoMarketingVO> goodsInfos = new ArrayList<>();
}
