package com.wanmi.sbc.goods.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.request.goods.GoodsByIdRequest
 * 根据编号查询商品信息请求对象
 * @author lipeng
 * @dateTime 2018/11/5 上午9:40
 */
@ApiModel
@Data
public class GoodsViewByIdAndSkuIdsRequest implements Serializable {

    private static final long serialVersionUID = 5594325220431537194L;

    @ApiModelProperty(value = "商品Id")
    @NotBlank
    private String goodsId;

    @ApiModelProperty(value = "skuId集合")
    @NotNull
    private List<String> skuIds;
}
