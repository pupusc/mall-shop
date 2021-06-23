package com.wanmi.sbc.goods.api.request.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.request.goodscate.GoodsCateExistsChildByIdRequest
 * 根据编号查询当前分类下面是否存在子分类请求对象
 * @author lipeng
 * @dateTime 2018/11/1 下午5:16
 */
@ApiModel
@Data
public class GoodsCateExistsChildByIdRequest implements Serializable {

    private static final long serialVersionUID = 7589155220211448594L;

    @ApiModelProperty(value = "分类Id")
    private Long cateId;
}
