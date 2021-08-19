package com.wanmi.sbc.marketing.api.request.gift;

import com.wanmi.sbc.marketing.api.request.market.MarketingIdRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class FullGiftDetailListByMarketingIdAndLevelIdRequest extends MarketingIdRequest {

    private static final long serialVersionUID = -1909549805523668477L;
    /**
     * 满赠等级id
     */
    @ApiModelProperty(value = "满赠等级id")
    private Long giftLevelId;


}
