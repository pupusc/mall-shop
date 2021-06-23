package com.wanmi.sbc.goods.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @discription 根据sku查询商品简易信息
 * @author yangzhen
 * @date 2020/9/8 11:40
 * @param
 * @return
 */
@ApiModel
@Data
public class GoodsDetailSimpleRequest implements Serializable {

    private static final long serialVersionUID = 5594325220431537194L;

    @ApiModelProperty(value = "商品Id")
    @NotBlank
    private String goodsId;

}
