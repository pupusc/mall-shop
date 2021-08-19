package com.wanmi.sbc.goods.api.response.info;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoSpecDetailRelVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据ids查询商品主要信息response
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsMainInfoByIdsResponse implements Serializable {

    private static final long serialVersionUID = 6243312906897496835L;

    /**
     * 商品SPU信息list
     */
    @ApiModelProperty(value = "商品SPU信息list")
    private List<GoodsVO> goodsVOList = new ArrayList<>();

    /**
     * 单品SPU信息list
     */
    @ApiModelProperty(value = "单品SPU信息list")
    private List<GoodsInfoVO> goodsInfoVOList = new ArrayList<>();

    /**
     * SKU与规格值关联信息list
     */
    @ApiModelProperty(value = "SKU与规格值关联信息list")
    private List<GoodsInfoSpecDetailRelVO> GoodsInfoSpecDetailRelVOList = new ArrayList<>();
}
