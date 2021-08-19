package com.wanmi.sbc.goods.provider.impl.common;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.common.GoodsCommonQueryProvider;
import com.wanmi.sbc.goods.api.request.common.InfoForPurchaseRequest;
import com.wanmi.sbc.goods.api.response.common.GoodsInfoForPurchaseResponse;
import com.wanmi.sbc.goods.common.GoodsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: wanggang
 * @createDate: 2018/11/2 9:52
 * @version: 1.0
 */
@RestController
@Validated
public class GoodsCommonQueryController implements GoodsCommonQueryProvider{

    @Autowired
    private GoodsCommonService goodsCommonService;

    /**
     * 递归方式，获取全局唯一SPU编码
     * @return SPU编码
     */

    @Override
    public BaseResponse<String> getSpuNoByUnique(){
      return BaseResponse.success(goodsCommonService.getSpuNoByUnique());
    }

    /**
     * 获取Spu编码
     * @return SPU编码
     */

    @Override
    public BaseResponse<String> getSpuNo(){
        return BaseResponse.success(goodsCommonService.getSpuNo());
    }

    /**
     * 递归方式，获取全局唯一SKU编码
     * @return SKU编码
     */

    @Override
    public BaseResponse<String> getSkuNoByUnique(){
        return BaseResponse.success(goodsCommonService.getSkuNoByUnique());
    }

    /**
     * 获取Sku编码
     * @return Sku编码
     */

    @Override
    public BaseResponse<String> getSkuNo(){
        return BaseResponse.success(goodsCommonService.getSkuNo());
    }

    @Override
    public BaseResponse<GoodsInfoForPurchaseResponse> queryInfoForPurchase(@RequestBody @Valid InfoForPurchaseRequest request) {
        return BaseResponse.success(goodsCommonService.queryInfoForPurchase(request));
    }
}
