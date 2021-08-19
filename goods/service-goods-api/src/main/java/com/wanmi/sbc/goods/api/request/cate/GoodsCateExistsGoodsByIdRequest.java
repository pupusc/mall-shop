package com.wanmi.sbc.goods.api.request.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.request.goodscate.GoodsCateExistsGoodsByIdRequest
 * 根据编号查询当前分类下面是否存在商品请求对象
 * @author lipeng
 * @dateTime 2018/11/2 上午9:12
 */
@ApiModel
@Data
public class GoodsCateExistsGoodsByIdRequest implements Serializable {

    private static final long serialVersionUID = -7374253986834777991L;

    @ApiModelProperty(value = "分类Id")
    private Long cateId;
}
