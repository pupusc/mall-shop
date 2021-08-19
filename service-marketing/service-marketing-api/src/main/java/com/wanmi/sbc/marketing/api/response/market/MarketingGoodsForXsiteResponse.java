package com.wanmi.sbc.marketing.api.response.market;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketingGoodsForXsiteResponse {

    /**
     * 商品id列表
     */
    @ApiModelProperty(value = "商品id列表")
    private List<String> goodsInfoIds;
}
