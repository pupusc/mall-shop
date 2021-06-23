package com.wanmi.sbc.elastic.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品SKU查询请求
 * Created by daiyitian on 2017/3/24.
 */
@Data
@ApiModel
public class EsDistributorGoodsListQueryRequest implements Serializable {

    /**
     * 批量SKU编号
     */
    @ApiModelProperty(value = "批量SKU编号")
    private List<String> goodsIdList;

    @ApiModelProperty(value = "分页请求Request")
    private EsGoodsInfoQueryRequest request;

}
