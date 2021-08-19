package com.wanmi.sbc.order.follow.reponse;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品SKU视图响应
 * Created by daiyitian on 2017/3/24.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsCustomerFollowResponse {

    /**
     * 商品SKU信息
     */
    private MicroServicePage<GoodsInfoVO> goodsInfos ;

    /**
     * 商品SPU信息
     */
    private List<GoodsVO> goodses = new ArrayList<>();

    /**
     * 商品区间价格列表
     */
    private List<GoodsIntervalPriceVO> goodsIntervalPrices = new ArrayList<>();

}
