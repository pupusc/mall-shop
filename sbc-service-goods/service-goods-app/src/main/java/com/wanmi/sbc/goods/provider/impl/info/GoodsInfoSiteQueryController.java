package com.wanmi.sbc.goods.provider.impl.info;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoSiteQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoDetailByGoodsInfoResponse;
import com.wanmi.sbc.goods.common.SystemPointsConfigService;
import com.wanmi.sbc.goods.goodslabel.service.GoodsLabelService;
import com.wanmi.sbc.goods.info.reponse.GoodsInfoDetailResponse;
import com.wanmi.sbc.goods.info.service.GoodsInfoSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;

/**
 * @author: wanggang
 * @createDate: 2018/11/1 14:14
 * @version: 1.0
 */
@RestController
@Validated
public class GoodsInfoSiteQueryController implements GoodsInfoSiteQueryProvider {

    @Autowired
    private GoodsInfoSiteService goodsInfoSiteService;

    @Autowired
    private SystemPointsConfigService systemPointsConfigService;

    @Autowired
    private GoodsLabelService goodsLabelService;

    /**
     * 获取商品详情
     * 计算会员和订货区间
     *
     * @param goodsInfoRequest {@link GoodsInfoRequest }
     * @return 商品详情 {@link GoodsInfoDetailByGoodsInfoResponse }
     */
    
    @Override
    public BaseResponse<GoodsInfoDetailByGoodsInfoResponse> getByGoodsInfo(@RequestBody @Valid GoodsInfoRequest goodsInfoRequest) {
        GoodsInfoDetailResponse goodsInfoDetailResponse = goodsInfoSiteService.detail(goodsInfoRequest);
        GoodsInfoDetailByGoodsInfoResponse goodsInfoDetailByGoodsInfoResponse = GoodsInfoConvert.toGoodsInfoDetailByGoodsInfoResponse(goodsInfoDetailResponse);

        //控制是否显示商品标签
        if(Boolean.TRUE.equals(goodsInfoRequest.getShowLabelFlag())){
            goodsLabelService.fillGoodsLabel(Collections.singletonList(goodsInfoDetailByGoodsInfoResponse.getGoods()),
                    goodsInfoRequest.getShowSiteLabelFlag());
        }
        systemPointsConfigService.clearBuyPoinsForSku(goodsInfoDetailByGoodsInfoResponse.getGoodsInfo());
        return BaseResponse.success(goodsInfoDetailByGoodsInfoResponse);
    }
}
