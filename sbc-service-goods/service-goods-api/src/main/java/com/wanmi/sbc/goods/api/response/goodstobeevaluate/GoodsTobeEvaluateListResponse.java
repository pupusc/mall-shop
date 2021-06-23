package com.wanmi.sbc.goods.api.response.goodstobeevaluate;

import com.wanmi.sbc.goods.bean.vo.GoodsTobeEvaluateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>订单商品待评价列表结果</p>
 * @author lzw
 * @date 2019-03-20 14:47:38
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsTobeEvaluateListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单商品待评价列表结果
     */
    @ApiModelProperty(value = "订单商品待评价列表结果")
    private List<GoodsTobeEvaluateVO> goodsTobeEvaluateVOList;
}
