package com.wanmi.sbc.goods.provider.impl.price;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.price.GoodsIntervalPriceQueryProvider;
import com.wanmi.sbc.goods.api.request.price.GoodsIntervalPriceListBySkuIdsRequest;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceListBySkuIdsResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import com.wanmi.sbc.goods.price.service.GoodsIntervalPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 商品区间价查询服务
 * @author daiyitian
 * @dateTime 2018/11/13 14:57
 */
@RestController
@Validated
public class GoodsIntervalPriceQueryController implements GoodsIntervalPriceQueryProvider {

    @Autowired
    private GoodsIntervalPriceService goodsIntervalPriceService;

    /**
     * 根据skuIds批量查询商品区间价列表
     *
     * @param request 包含skuIds的查询请求结构 {@link GoodsIntervalPriceListBySkuIdsRequest }
     * @return 商品区间价列表 {@link GoodsIntervalPriceListBySkuIdsResponse }
     */
    @Override
    public BaseResponse<GoodsIntervalPriceListBySkuIdsResponse> listByGoodsIds(@RequestBody @Valid
                                                                                GoodsIntervalPriceListBySkuIdsRequest
                                                                                request) {
        List<GoodsIntervalPriceVO> voList =
                KsBeanUtil.convertList(goodsIntervalPriceService.findBySkuIds(request.getSkuIds()),
                        GoodsIntervalPriceVO.class);
        return BaseResponse.success(GoodsIntervalPriceListBySkuIdsResponse.builder()
                .goodsIntervalPriceVOList(voList).build());
    }
}
