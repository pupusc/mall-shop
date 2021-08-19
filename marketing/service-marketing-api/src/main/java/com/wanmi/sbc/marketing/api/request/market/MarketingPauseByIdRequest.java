package com.wanmi.sbc.marketing.api.request.market;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-19 14:43
 */
@ApiModel
@Data
@Builder
public class MarketingPauseByIdRequest extends MarketingIdRequest{

    private static final long serialVersionUID = 5684402430642494288L;
}
