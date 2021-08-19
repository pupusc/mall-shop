package com.wanmi.sbc.goods.api.response.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.response.goods.GoodsAddAllResponse
 * 新增商品基本信息、基价响应对象
 * @author lipeng
 * @dateTime 2018/11/5 上午10:39
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsSynResponse implements Serializable {

    private static final long serialVersionUID = 3911745008051800304L;

    @ApiModelProperty(value = "商品Id")
    private List<String> goodsIds = new ArrayList<>();
}
