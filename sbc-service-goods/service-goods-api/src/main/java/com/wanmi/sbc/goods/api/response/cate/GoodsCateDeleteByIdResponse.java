package com.wanmi.sbc.goods.api.response.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.response.goodscate.GoodsCateDeleteByIdResponse
 * 根据分类编号删除分类信息响应对象
 * @author lipeng
 * @dateTime 2018/11/1 下午4:59
 */
@ApiModel
@Data
public class GoodsCateDeleteByIdResponse implements Serializable {

    private static final long serialVersionUID = 2746012805117761001L;

    @ApiModelProperty(value = "商品分类Id")
    private List<Long> delIdList;
}
