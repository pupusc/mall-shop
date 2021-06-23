package com.wanmi.sbc.goods.api.request.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.request.info.DistributionGoodsForbidRequest
 * 禁止分销商品请求对象
 *
 * @author CHENLI
 * @dateTime 2019/2/19 上午9:33
 */
@ApiModel
@Data
public class DistributionGoodsForbidRequest implements Serializable {

    private static final long serialVersionUID = 1545831207821143601L;

    /**
     * 审核分销商品，skuId
     */
    @ApiModelProperty(value = "审核分销商品，skuId")
    @NotBlank
    private String goodsInfoId;

    /**
     * 禁止分销商品原因
     */
    @NotBlank
    @ApiModelProperty(value = "禁止分销商品原因")
    private String distributionGoodsAuditReason;
}
