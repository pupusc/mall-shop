package com.wanmi.sbc.marketing.api.request.discount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p></p>
 * Date: 2018-11-20
 * @author Administrator
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketingFullReductionByMarketingIdRequest implements Serializable{

    private static final long serialVersionUID = -8493934199648964530L;

    @ApiModelProperty(value = "营销id")
    private Long marketingId;
}
