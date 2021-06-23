package com.wanmi.sbc.goods.api.request.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.request.info.DistributionGoodsCheckRequest
 * 审核分销商品请求对象
 *
 * @author CHENLI
 * @dateTime 2019/2/19 上午9:33
 */
@ApiModel
@Data
public class DistributionGoodsCheckRequest implements Serializable {

    private static final long serialVersionUID = -1718348722977246357L;

    /**
     * 审核分销商品，skuId
     */
    @NotBlank
    @ApiModelProperty(value = "审核分销商品，skuId")
    private String goodsInfoId;
}
