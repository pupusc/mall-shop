package com.wanmi.sbc.goods.api.response.goods;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.GoodsForXsiteVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sbc-micro-service
 * @description:
 * @create: 2020-02-28 17:14
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsPageForXsiteResponse implements Serializable {
    private static final long serialVersionUID = -505939560400957287L;

    /**
     * 商品分页数据
     */
    @ApiModelProperty(value = "商品分页数据")
    private MicroServicePage<GoodsForXsiteVO> goodsForXsiteVOPage;
}