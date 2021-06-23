package com.wanmi.sbc.goods.api.response.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * com.wanmi.sbc.goods.api.response.goods.GoodsModifyResponse
 * 修改商品响应对象
 * @author lipeng
 * @dateTime 2018/11/5 上午10:24
 */
@ApiModel
@Data
public class GoodsModifyResponse implements Serializable {

    private static final long serialVersionUID = 1005892526767405136L;

    @ApiModelProperty(value = "商品信息", notes = "newGoodsInfo: 新商品信息, " +
            "delInfoIds: 删除的商品信息, oldGoodsInfos: 老商品信息, delStoreGoodsInfoIds:删除的商家商品信息 " )
    private Map<String, Object> returnMap;

    /**
     * 商品库id
     */
    @ApiModelProperty(value = "商品库id")
    private List<String> standardIds;
}
