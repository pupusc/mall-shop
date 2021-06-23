package com.wanmi.sbc.goods.api.response.goodstobeevaluate;

import com.wanmi.sbc.goods.bean.vo.GoodsTobeEvaluateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）订单商品待评价信息response</p>
 * @author lzw
 * @date 2019-03-20 14:47:38
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsTobeEvaluateByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单商品待评价信息
     */
    @ApiModelProperty(value = "订单商品待评价信息")
    private GoodsTobeEvaluateVO goodsTobeEvaluateVO;
}
