package com.wanmi.sbc.goods.api.response.price;

import com.wanmi.sbc.goods.bean.vo.GoodsLevelPriceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsLevelPriceBySkuIdsAndLevelIdsResponse implements Serializable {

    private static final long serialVersionUID = 4314927986900356364L;

    @ApiModelProperty(value = "商品级别价格")
    private  List<GoodsLevelPriceVO> goodsLevelPriceList;
}
