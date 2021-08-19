package com.wanmi.sbc.marketing.api.response.plugin;

import com.wanmi.sbc.marketing.bean.vo.TradeMarketingWrapperVO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 批量订单提交营销处理响应结构
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class MarketingCouponWrapperResponse extends TradeMarketingWrapperVO {

    private static final long serialVersionUID = 4520038630632475443L;

}
