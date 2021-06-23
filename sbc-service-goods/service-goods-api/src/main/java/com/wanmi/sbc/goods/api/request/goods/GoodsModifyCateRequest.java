package com.wanmi.sbc.goods.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.request.goods.GoodsModifyCateRequest
 * 修改商品分类请求对象
 * @author lipeng
 * @dateTime 2018/11/5 上午10:54
 */
@ApiModel
@Data
public class GoodsModifyCateRequest implements Serializable {

    private static final long serialVersionUID = -8844597130119524324L;

    @ApiModelProperty(value = "商品id")
    private List<String> goodsIds;

    @ApiModelProperty(value = "店铺分类id")
    private List<Long> storeCateIds;
}
