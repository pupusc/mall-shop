package com.soybean.mall.goods.vo;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsSpecDetailVO;
import com.wanmi.sbc.goods.bean.vo.GoodsSpecVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SpuSpecsResultVO implements Serializable {
    /**
     * 商品规格列表
     */
    private List<GoodsSpecVO> goodsSpecs;

    /**
     * 商品规格值列表
     */
    private List<GoodsSpecDetailVO> goodsSpecDetails;

    /**
     * 商品SKU列表
     */
    private List<GoodsInfoVO> goodsInfos;
}
