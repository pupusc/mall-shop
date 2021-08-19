package com.wanmi.sbc.goods.api.request.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.request.info.DistributionGoodsBatchCheckRequest
 * 批量审核分销商品请求对象
 *
 * @author CHENLI
 * @dateTime 2019/2/19 上午9:33
 */
@ApiModel
@Data
public class DistributionGoodsBatchCheckRequest implements Serializable {

    private static final long serialVersionUID = -6081625447225022375L;

    /**
     * 批量审核分销商品，skuIds
     */
    @ApiModelProperty(value = "批量skuIds")
    @NotNull
    private List<String> goodsInfoIds;
}
