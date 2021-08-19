package com.wanmi.sbc.goods.provider.impl.marketing;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.marketing.GoodsMarketingProvider;
import com.wanmi.sbc.goods.api.request.marketing.GoodsMarketingBatchAddRequest;
import com.wanmi.sbc.goods.api.request.marketing.GoodsMarketingDeleteByCustomerIdAndGoodsInfoIdsRequest;
import com.wanmi.sbc.goods.api.request.marketing.GoodsMarketingDeleteByCustomerIdRequest;
import com.wanmi.sbc.goods.api.request.marketing.GoodsMarketingModifyRequest;
import com.wanmi.sbc.goods.api.response.marketing.GoodsMarketingModifyResponse;
import com.wanmi.sbc.goods.marketing.model.data.GoodsMarketing;
import com.wanmi.sbc.goods.marketing.service.GoodsMarketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description: 商品营销控制器
 * @Date: 2018-11-07 13:46
 */
@Validated
@RestController
public class GoodsMarketingController implements GoodsMarketingProvider {

    @Autowired
    private GoodsMarketingService goodsMarketingService;

    /**
     * @param request 根据用户编号删除商品使用的营销 {@link GoodsMarketingDeleteByCustomerIdRequest}
     * @return
     */
    @Override
    
    public BaseResponse deleteByCustomerId(@RequestBody @Valid GoodsMarketingDeleteByCustomerIdRequest request) {
        return BaseResponse.success(goodsMarketingService.delByCustomerId(request.getCustomerId()));
    }

    /**
     * @param request 根据用户编号和商品编号列表删除商品使用的营销 {@link GoodsMarketingDeleteByCustomerIdAndGoodsInfoIdsRequest}
     * @return
     */
    @Override
    
    public BaseResponse deleteByCustomerIdAndGoodsInfoIds(@RequestBody @Valid GoodsMarketingDeleteByCustomerIdAndGoodsInfoIdsRequest request) {
        return BaseResponse.success(goodsMarketingService.delByCustomerIdAndGoodsInfoIds(
                request.getCustomerId(), request.getGoodsInfoIds()));
    }

    /**
     * @param request 批量添加商品使用的营销 {@link GoodsMarketingBatchAddRequest}
     * @return
     */
    @Override
    
    public BaseResponse batchAdd(@RequestBody @Valid GoodsMarketingBatchAddRequest request) {


        List<GoodsMarketing> goodsMarketings =  KsBeanUtil.convertList(
                request.getGoodsMarketings(), GoodsMarketing.class);

        return BaseResponse.success(goodsMarketingService.batchAdd(goodsMarketings));
    }

    /**
     * @param request 修改商品使用的营销 {@link GoodsMarketingModifyRequest}
     * @return
     */
    @Override
    
    public BaseResponse<GoodsMarketingModifyResponse> modify(@RequestBody @Valid GoodsMarketingModifyRequest request) {

        GoodsMarketing goodsMarketing = new GoodsMarketing();
        KsBeanUtil.copyPropertiesThird(request, goodsMarketing);

        GoodsMarketingModifyResponse goodsMarketingModifyResponse = new GoodsMarketingModifyResponse();
        KsBeanUtil.copyPropertiesThird(goodsMarketingService.modify(goodsMarketing), goodsMarketingModifyResponse);

        return BaseResponse.success(goodsMarketingModifyResponse);
    }

}
