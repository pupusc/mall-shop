package com.wanmi.sbc.marketing.api.response.market;

import com.wanmi.sbc.marketing.bean.vo.MarketingSuitsGoodsInfoDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingGoodsInfoResponse {

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String  goodsInfoId;

    /**
     * 商品套餐数量
     */
    @ApiModelProperty(value = "商品套餐数量")
    private  Long goodsInfoRelationSuitsNum;

    /**
     * [{套餐一},{套餐二}...]
     */
    @ApiModelProperty(value = "商品关联组合套餐集合")
    private List<MarketingSuitsGoodsInfoDetailVO> marketingSuitsGoodsInfoDetailVOList;

}
