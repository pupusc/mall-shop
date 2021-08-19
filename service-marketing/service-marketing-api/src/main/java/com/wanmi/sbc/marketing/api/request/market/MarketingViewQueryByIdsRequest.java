package com.wanmi.sbc.marketing.api.request.market;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-19 15:06
 */
@ApiModel
@Data
public class MarketingViewQueryByIdsRequest extends MarketingIdsRequest{

    private static final long serialVersionUID = -4437750863899344273L;

    /**
     * 是否需要规则信息
     */
    @ApiModelProperty(value = "是否需要规则信息")
    private Boolean levelFlag;
}
