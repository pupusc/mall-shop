package com.wanmi.sbc.marketing.api.response.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * <p>验证开店礼包商品状态</p>
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionSetting4StoreBagsVerifyResponse implements Serializable {
    private static final long serialVersionUID = -4242586400290324856L;
    @ApiModelProperty(value = "验证开店礼包商品状态")
    private Boolean result;
}
