package com.wanmi.sbc.goods.api.request.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.request.info.DistributionGoodsDeleteRequest
 * 删除分销商品请求对象
 *
 * @author CHENLI
 * @dateTime 2019/2/19 上午9:33
 */
@ApiModel
@Data
public class DistributionGoodsDeleteRequest implements Serializable {

    private static final long serialVersionUID = 6921807898394054433L;

    /**
     * 删除分销商品，skuId
     */
    @NotBlank
    @ApiModelProperty(value = "删除分销商品，skuId")
    private String goodsInfoId;

    /**
     * sku编码
     */
    @ApiModelProperty(value = "sku编码")
    private String goodsInfoNo;
}
