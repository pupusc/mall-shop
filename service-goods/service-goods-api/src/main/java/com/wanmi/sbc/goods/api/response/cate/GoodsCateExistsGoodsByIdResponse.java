package com.wanmi.sbc.goods.api.response.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.response.goodscate.GoodsCateExistsGoodsByIdResponse
 * 根据编号查询当前分类下面是否存在商品响应对象
 * @author lipeng
 * @dateTime 2018/11/2 上午9:13
 */
@ApiModel
@Data
public class GoodsCateExistsGoodsByIdResponse implements Serializable {

    private static final long serialVersionUID = -948307630747073816L;

    @ApiModelProperty(value = "是否存在商品", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer result;
}
