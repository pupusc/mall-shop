package com.wanmi.sbc.goods.api.request.spec;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: wanggang
 * @createDate: 2018/11/13 14:59
 * @version: 1.0
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest implements Serializable {
    private static final long serialVersionUID = -1780491183044180754L;

    @ApiModelProperty(value = "spu Id")
    private String goodsId;

    @ApiModelProperty(value = "sku Id")
    private String goodsInfoId;
}
