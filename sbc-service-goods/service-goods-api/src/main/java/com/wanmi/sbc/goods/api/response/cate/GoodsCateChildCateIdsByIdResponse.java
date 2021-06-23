package com.wanmi.sbc.goods.api.response.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.response.goodscate.GoodsCateChildCateIdByIdResponse
 * 查询当前分类下面所有子分类编号响应对象
 * @author lipeng
 * @dateTime 2018/11/2 上午9:30
 */
@ApiModel
@Data
public class GoodsCateChildCateIdsByIdResponse implements Serializable {

    private static final long serialVersionUID = -1134157206393289732L;

    @ApiModelProperty(value = "商品分类Id")
    private List<Long> childCateIdList;
}
