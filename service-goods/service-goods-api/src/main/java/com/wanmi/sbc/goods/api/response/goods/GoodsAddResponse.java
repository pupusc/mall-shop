package com.wanmi.sbc.goods.api.response.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.response.goods.GoodsAddResponse
 * 新增商品响应对象
 * @author lipeng
 * @dateTime 2018/11/5 上午10:02
 */
@ApiModel
@Data
public class GoodsAddResponse implements Serializable {

    private static final long serialVersionUID = 2113304714372754068L;

    @ApiModelProperty(value = "新增商品响应对象")
    private String result;
}
