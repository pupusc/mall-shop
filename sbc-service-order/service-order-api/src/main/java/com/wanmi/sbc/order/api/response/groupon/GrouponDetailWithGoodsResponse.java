package com.wanmi.sbc.order.api.response.groupon;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>团明细</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponDetailWithGoodsResponse implements Serializable {
    private static final long serialVersionUID = -9076705454351078810L;
    /**
     * 商品Sku列表
     */
    @ApiModelProperty(value = "商品Sku列表")
    private List<GoodsInfoVO> goodsInfoVOList;
}
