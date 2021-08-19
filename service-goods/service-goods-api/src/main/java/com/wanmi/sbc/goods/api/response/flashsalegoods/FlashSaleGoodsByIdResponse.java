package com.wanmi.sbc.goods.api.response.flashsalegoods;

import com.wanmi.sbc.goods.bean.vo.FlashSaleGoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据id查询任意（包含已删除）抢购商品表信息response</p>
 * @author bob
 * @date 2019-06-11 14:54:31
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlashSaleGoodsByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 抢购商品表信息
     */
    @ApiModelProperty(value = "抢购商品表信息")
    private FlashSaleGoodsVO flashSaleGoodsVO;
}
