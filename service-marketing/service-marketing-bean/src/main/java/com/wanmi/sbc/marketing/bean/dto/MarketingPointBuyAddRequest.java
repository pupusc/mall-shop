package com.wanmi.sbc.marketing.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MarketingPointBuyAddRequest extends MarketingSaveDTO {

    /**
     * 积分换购信息
     */
    @ApiModelProperty(value = "积分换购信息")
    private List<MarketingPointBuyLevelDto> marketingPointBuyLevel;
}
