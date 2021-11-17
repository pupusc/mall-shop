package com.wanmi.sbc.marketing.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MarketingPointBuyAddRequest extends MarketingSaveDTO {

    /**
     * 积分换购信息
     */
    @ApiModelProperty(value = "积分换购信息")
    private MarketingPointBuyLevelDto marketingPointBuyLevel;
}
