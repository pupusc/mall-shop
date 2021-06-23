package com.wanmi.sbc.goods.api.response.price;

import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 根据skuIds查询区间价列表响应
 *
 * @author daiyitian
 * @dateTime 2018/11/13 上午9:49
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsIntervalPriceListBySkuIdsResponse implements Serializable {

    private static final long serialVersionUID = 5266547069045715872L;

    @ApiModelProperty(value = "商品订货区间价格")
    private List<GoodsIntervalPriceVO> goodsIntervalPriceVOList;
}
