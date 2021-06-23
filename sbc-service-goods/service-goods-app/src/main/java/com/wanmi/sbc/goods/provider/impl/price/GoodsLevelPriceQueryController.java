package com.wanmi.sbc.goods.provider.impl.price;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.price.GoodsLevelPriceQueryProvider;
import com.wanmi.sbc.goods.api.request.price.GoodsLevelPriceBySkuIdsAndLevelIdsRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsLevelPriceBySkuIdsRequest;
import com.wanmi.sbc.goods.api.response.price.GoodsLevelPriceBySkuIdsAndLevelIdsResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsLevelPriceBySkuIdsResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsLevelPriceVO;
import com.wanmi.sbc.goods.price.model.root.GoodsLevelPrice;
import com.wanmi.sbc.goods.price.service.GoodsLevelPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;


@RestController
@Validated
public class GoodsLevelPriceQueryController implements GoodsLevelPriceQueryProvider {

    @Autowired
    private GoodsLevelPriceService goodsLevelPriceService;

    /**
     * 根据批量商品ID和批量等级查询SKU的级别价
     * @param goodsLevelPriceBySkuIdsAndLevelIdsRequest 包含：商品ID和会员等级ID {@link GoodsLevelPriceBySkuIdsAndLevelIdsRequest }
     * @return {@link GoodsLevelPriceBySkuIdsAndLevelIdsResponse }
     */
    
    @Override
    public BaseResponse<GoodsLevelPriceBySkuIdsAndLevelIdsResponse> listBySkuIdsAndLevelIds(@RequestBody @Valid GoodsLevelPriceBySkuIdsAndLevelIdsRequest goodsLevelPriceBySkuIdsAndLevelIdsRequest) {
        List<GoodsLevelPrice> goodsLevelPriceList =  goodsLevelPriceService.getSkuLevelPriceByGoodsInfoIdAndLevelIds(goodsLevelPriceBySkuIdsAndLevelIdsRequest.getSkuIds(),goodsLevelPriceBySkuIdsAndLevelIdsRequest.getLevelIds());
        if (CollectionUtils.isEmpty(goodsLevelPriceList)){
            return BaseResponse.success(new GoodsLevelPriceBySkuIdsAndLevelIdsResponse(Collections.emptyList()));
        }
        List<GoodsLevelPriceVO> goodsLevelPriceVOList = KsBeanUtil.convert(goodsLevelPriceList,GoodsLevelPriceVO.class);
        return BaseResponse.success(new GoodsLevelPriceBySkuIdsAndLevelIdsResponse(goodsLevelPriceVOList));
    }

    /**
     * 根据商品SkuID查询SKU的级别价
     * @param goodsLevelPriceBySkuIdsRequest 包含：skuID {@link GoodsLevelPriceBySkuIdsRequest }
     * @return {@link GoodsLevelPriceBySkuIdsResponse }
     */
    
    @Override
    public BaseResponse<GoodsLevelPriceBySkuIdsResponse> listBySkuIds(@RequestBody @Valid GoodsLevelPriceBySkuIdsRequest goodsLevelPriceBySkuIdsRequest) {
        List<GoodsLevelPrice> goodsLevelPriceList =  goodsLevelPriceService.findSkuByGoodsInfoIds(goodsLevelPriceBySkuIdsRequest.getSkuIds(), goodsLevelPriceBySkuIdsRequest.getType());
        if (CollectionUtils.isEmpty(goodsLevelPriceList)){
            return BaseResponse.success(new GoodsLevelPriceBySkuIdsResponse(Collections.emptyList()));
        }
        List<GoodsLevelPriceVO> goodsLevelPriceVOList = KsBeanUtil.convert(goodsLevelPriceList,GoodsLevelPriceVO.class);
        return BaseResponse.success(new GoodsLevelPriceBySkuIdsResponse(goodsLevelPriceVOList));
    }
}
