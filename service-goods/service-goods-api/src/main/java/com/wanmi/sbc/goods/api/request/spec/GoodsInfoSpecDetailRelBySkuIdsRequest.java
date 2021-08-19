package com.wanmi.sbc.goods.api.request.spec;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author: wanggang
 * @createDate: 2018/11/13 14:59
 * @version: 1.0
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsInfoSpecDetailRelBySkuIdsRequest implements Serializable {

    private static final long serialVersionUID = 4319103692467979947L;

    @ApiModelProperty(value = "sku Id")
    private List<String> goodsInfoIds;
}
