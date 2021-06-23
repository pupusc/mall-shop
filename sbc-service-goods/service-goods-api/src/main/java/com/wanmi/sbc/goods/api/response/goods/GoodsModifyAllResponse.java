package com.wanmi.sbc.goods.api.response.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * com.wanmi.sbc.goods.api.response.goods.GoodsModifyAllResponse
 *
 * @author lipeng
 * @dateTime 2018/11/5 上午10:42
 */
@ApiModel
@Data
public class GoodsModifyAllResponse implements Serializable {

    private static final long serialVersionUID = 371087078916252371L;

    @ApiModelProperty(value = "商品信息", notes = "newGoodsInfo: 新商品信息, " +
            "delInfoIds: 删除的商品信息, oldGoodsInfos: 老商品信息")
    private Map<String, Object> resultMap;
}
