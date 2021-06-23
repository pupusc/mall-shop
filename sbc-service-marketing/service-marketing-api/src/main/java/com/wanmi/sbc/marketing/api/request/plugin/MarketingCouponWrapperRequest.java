package com.wanmi.sbc.marketing.api.request.plugin;

import com.wanmi.sbc.marketing.bean.dto.TradeMarketingWrapperDTO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 批量订单提交营销处理请求结构
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class MarketingCouponWrapperRequest extends TradeMarketingWrapperDTO {

    private static final long serialVersionUID = 4520038630632475443L;

}
