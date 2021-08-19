package com.wanmi.sbc.goods.api.request.enterprise.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

@Data
@ApiModel
public class EnterprisePriceGetRequest {

    @NotEmpty
    @ApiModelProperty("商品SKU列表--商品列表页使用")
    private List<String> goodsInfoIds;

    @ApiModelProperty("购买人")
    private String customerId;

    /**
     * key goodsInfoId
     * value 购买数量
     */
    @ApiModelProperty("对应商品的购买数量")
    private Map<String, Long> buyCountMap;

    /**
     * 是否是列表展示
     */
    @ApiModelProperty
    private Boolean listFlag = false;

    /**
     * 是否是下单
     */
    @ApiModelProperty
    private Boolean orderFlag = false;
}
