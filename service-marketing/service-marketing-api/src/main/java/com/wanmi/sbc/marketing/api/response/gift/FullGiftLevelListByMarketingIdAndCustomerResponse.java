package com.wanmi.sbc.marketing.api.response.gift;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullGiftLevelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-20 16:44
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullGiftLevelListByMarketingIdAndCustomerResponse {

    /**
     * 活动规则列表
     */
    @ApiModelProperty(value = "活动规则列表")
    private List<MarketingFullGiftLevelVO> levelList;

    /**
     * 赠品列表
     */
    @ApiModelProperty(value = "赠品列表")
    private List<GoodsInfoVO> giftList;

    /**
     * 活动子类型
     */
    @ApiModelProperty(value = "活动子类型")
    private MarketingSubType marketingSubType;

}
