package com.wanmi.sbc.goods.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @discription 根据sku查询商品图文信息和属性
 * @author yangzhen
 * @date 2020/9/3 11:40
 * @param
 * @return
 */
@ApiModel
@Data
public class GoodsDetailProperBySkuIdRequest implements Serializable {

    private static final long serialVersionUID = 5594325220431537194L;

    @ApiModelProperty(value = "skuId")
    @NotBlank
    private String skuId;
}
