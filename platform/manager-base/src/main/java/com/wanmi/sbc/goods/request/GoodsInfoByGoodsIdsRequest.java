package com.wanmi.sbc.goods.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Created by feitingting on 2019/1/8.
 */
@ApiModel
@Data
public class GoodsInfoByGoodsIdsRequest {
    /**
     * sku编号
     */
    @NotEmpty
    @ApiModelProperty(value = "sku编号")
    private List<String> goodsIds;
}

