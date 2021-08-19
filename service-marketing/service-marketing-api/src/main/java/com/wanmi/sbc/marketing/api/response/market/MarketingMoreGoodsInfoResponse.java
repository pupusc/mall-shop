package com.wanmi.sbc.marketing.api.response.market;

import com.wanmi.sbc.marketing.bean.vo.MarketingSuitsGoodsInfoDetailVO;
import com.wanmi.sbc.marketing.bean.vo.SuitsRelationGoodsInfoVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketingMoreGoodsInfoResponse {

    /**
     * 组合套餐活动
     */
    private MarketingSuitsGoodsInfoDetailVO marketingSuitsGoodsInfoDetailVO;


    /**
     * 组合套餐关联商品详情
     */
    private List<SuitsRelationGoodsInfoVO> suitsRelationGoodsInfoVOList;

}
