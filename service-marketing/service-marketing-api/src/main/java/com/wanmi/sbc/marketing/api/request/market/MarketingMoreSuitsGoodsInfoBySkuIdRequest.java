package com.wanmi.sbc.marketing.api.request.market;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class MarketingMoreSuitsGoodsInfoBySkuIdRequest extends BaseRequest {

    private static final long serialVersionUID = -9131130806085266917L;
    /**
     *  当前商品SkuId
     */
    @ApiModelProperty(value = "商品skuid")
    @NotNull
    private String  goodsInfoId;


}
