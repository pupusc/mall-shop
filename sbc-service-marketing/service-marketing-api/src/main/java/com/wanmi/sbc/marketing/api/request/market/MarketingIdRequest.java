package com.wanmi.sbc.marketing.api.request.market;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-19 15:09
 */
@ApiModel
@Data
public class MarketingIdRequest implements Serializable {

    private static final long serialVersionUID = -2038136314263588527L;
    /**
     * 营销ID
     */
    @ApiModelProperty(value = "营销Id")
    //@NotNull
    private Long marketingId;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

}
