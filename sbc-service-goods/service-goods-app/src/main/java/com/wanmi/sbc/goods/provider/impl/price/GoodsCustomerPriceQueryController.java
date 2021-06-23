package com.wanmi.sbc.goods.provider.impl.price;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.price.GoodsCustomerPriceQueryProvider;
import com.wanmi.sbc.goods.api.request.price.GoodsCustomerPriceBySkuIdsAndCustomerIdRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsCustomerPriceBySkuIdsRequest;
import com.wanmi.sbc.goods.api.response.price.GoodsCustomerPriceBySkuIdsAndCustomerIdResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsCustomerPriceBySkuIdsResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsCustomerPriceVO;
import com.wanmi.sbc.goods.price.model.root.GoodsCustomerPrice;
import com.wanmi.sbc.goods.price.service.GoodsCustomerPriceService;
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
public class GoodsCustomerPriceQueryController implements GoodsCustomerPriceQueryProvider {

    @Autowired
    private GoodsCustomerPriceService goodsCustomerPriceService;

    /**
     * 根据批量商品ID和客户ID查询商品会员价格
     * @param goodsCustomerPriceBySkuIdsAndCustomerIdRequest 包含：商品ID和会员ID {@link GoodsCustomerPriceBySkuIdsAndCustomerIdRequest }
     * @return {@link GoodsCustomerPriceBySkuIdsAndCustomerIdResponse }
     */
    
    @Override
    public BaseResponse<GoodsCustomerPriceBySkuIdsAndCustomerIdResponse> listBySkuIdsAndCustomerId(@RequestBody @Valid GoodsCustomerPriceBySkuIdsAndCustomerIdRequest goodsCustomerPriceBySkuIdsAndCustomerIdRequest) {
        List<GoodsCustomerPrice> goodsCustomerPriceList =  goodsCustomerPriceService.getSkuCustomerPriceByGoodsInfoIdAndCustomerId(goodsCustomerPriceBySkuIdsAndCustomerIdRequest.getSkuIds(),goodsCustomerPriceBySkuIdsAndCustomerIdRequest.getCustomerId());
        if (CollectionUtils.isEmpty(goodsCustomerPriceList)){
            return BaseResponse.success(new GoodsCustomerPriceBySkuIdsAndCustomerIdResponse(Collections.emptyList()));
        }
        List<GoodsCustomerPriceVO> goodsCustomerPriceVOList = KsBeanUtil.convert(goodsCustomerPriceList,GoodsCustomerPriceVO.class);
        return BaseResponse.success(new GoodsCustomerPriceBySkuIdsAndCustomerIdResponse(goodsCustomerPriceVOList));
    }

    /**
     * 根据商品SkuID查询SKU客户价
     * @param goodsCustomerPriceBySkuIdsRequest 包含：商品Sku ID {@link GoodsCustomerPriceBySkuIdsRequest }
     * @return {@link GoodsCustomerPriceBySkuIdsResponse }
     */
    
    @Override
    public BaseResponse<GoodsCustomerPriceBySkuIdsResponse> listBySkuIds(@RequestBody @Valid GoodsCustomerPriceBySkuIdsRequest goodsCustomerPriceBySkuIdsRequest) {
        List<GoodsCustomerPrice> goodsCustomerPriceList =  goodsCustomerPriceService.findSkuByGoodsInfoIds(goodsCustomerPriceBySkuIdsRequest.getSkuIds());
        if (CollectionUtils.isEmpty(goodsCustomerPriceList)){
            return BaseResponse.success(new GoodsCustomerPriceBySkuIdsResponse(Collections.emptyList()));
        }
        List<GoodsCustomerPriceVO> goodsCustomerPriceVOList = KsBeanUtil.convert(goodsCustomerPriceList,GoodsCustomerPriceVO.class);
        return BaseResponse.success(new GoodsCustomerPriceBySkuIdsResponse(goodsCustomerPriceVOList));
    }
}
