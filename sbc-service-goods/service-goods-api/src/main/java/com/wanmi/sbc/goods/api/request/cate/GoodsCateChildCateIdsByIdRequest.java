package com.wanmi.sbc.goods.api.request.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.request.goodscate.GoodsCateChildCateIdByIdRequest
 * 查询当前分类下面所有子分类编号请求对象
 * @author lipeng
 * @dateTime 2018/11/2 上午9:29
 */
@ApiModel
@Data
public class GoodsCateChildCateIdsByIdRequest implements Serializable {

    private static final long serialVersionUID = -7785782043249474823L;

    @ApiModelProperty(value = "分类Id")
    private Long cateId;
}
