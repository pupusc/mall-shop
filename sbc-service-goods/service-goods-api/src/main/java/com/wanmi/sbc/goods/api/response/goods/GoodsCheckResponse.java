package com.wanmi.sbc.goods.api.response.goods;

import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.request.goods.GoodsCheckRequest
 * 商品审核请求对象
 * @author lipeng
 * @dateTime 2018/11/5 上午11:18
 */
@ApiModel
@Data
public class GoodsCheckResponse implements Serializable {

    private static final long serialVersionUID = 5680162272600847L;

    /**
     * 商品库id
     */
    @ApiModelProperty(value = "商品库id")
    private List<String> standardIds;

    /**
     * 需要删除的商品库id
     */
    @ApiModelProperty(value = "需要删除的商品库id")
    private List<String> deleteStandardIds;
}
