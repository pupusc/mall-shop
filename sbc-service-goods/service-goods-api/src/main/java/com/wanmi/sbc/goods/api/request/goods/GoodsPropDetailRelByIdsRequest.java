package com.wanmi.sbc.goods.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.request.goods.GoodsPropDetailRelRequest
 * 根据多个SpuID查询属性关联请求对象
 * @author lipeng
 * @dateTime 2018/11/5 上午11:05
 */
@ApiModel
@Data
public class GoodsPropDetailRelByIdsRequest implements Serializable {

    private static final long serialVersionUID = 2683718144379132871L;

    @ApiModelProperty(value = "商品Id")
    private List<String> goodsIds;
}
