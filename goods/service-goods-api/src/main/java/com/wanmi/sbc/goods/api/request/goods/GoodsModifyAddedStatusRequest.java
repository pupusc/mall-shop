package com.wanmi.sbc.goods.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.request.goods.GoodsModifyAddedStatusRequest
 * 修改商品上下架状态请求对象
 * @author lipeng
 * @dateTime 2018/11/5 上午10:51
 */
@ApiModel
@Data
public class GoodsModifyAddedStatusRequest implements Serializable {

    private static final long serialVersionUID = -6250733406387197138L;

    @ApiModelProperty(value = "上下架状态", dataType = "com.wanmi.sbc.goods.bean.enums.AddedFlag")
    private Integer addedFlag;

    @ApiModelProperty(value = "商品Id")
    private List<String> goodsIds;
}
