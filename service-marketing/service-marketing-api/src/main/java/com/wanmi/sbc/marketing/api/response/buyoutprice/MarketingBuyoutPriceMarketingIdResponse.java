package com.wanmi.sbc.marketing.api.response.buyoutprice;

import com.wanmi.sbc.marketing.bean.vo.MarketingBuyoutPriceLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * Date: 2020-04-14
 * @author weiwenhao
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketingBuyoutPriceMarketingIdResponse extends MarketingVO implements Serializable{

    private static final long serialVersionUID = -3999146675129536228L;

    @ApiModelProperty(value = "一口价营销活动规则列表")
    private List<MarketingBuyoutPriceLevelVO> marketingBuyoutPriceLevelVO;


}
