package com.wanmi.sbc.marketing.api.response.market;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarketingMoreSuitsGoodInfoIdResponse implements Serializable {

    /**
     * 更多组合详情
     */
    @ApiModelProperty(value = "更多组合详情")
    private List <MarketingMoreGoodsInfoResponse> marketingMoreGoodsInfoResponseList;



}
