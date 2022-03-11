package com.soybean.mall.goods.controller;

import com.alipay.api.domain.GoodsInfo;
import com.soybean.mall.goods.response.GoodsDetailResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.constant.GoodsErrorCode;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsViewByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoByIdRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsViewByIdResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoByIdResponse;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController("wxGoodsController")
@RequestMapping("/wx/goods")
public class GoodsController {

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    /**
     * @description 商品详情页
     * @param spuId
     * @menu 小程序
     * @status done
     */
    @GetMapping("/detail")
    public BaseResponse<GoodsDetailResponse> detail(@RequestParam("spuId")String spuId, @RequestParam(value = "skuId",required = false)String skuId){
        GoodsViewByIdRequest request = new GoodsViewByIdRequest();
        request.setGoodsId(spuId);
        request.setShowLabelFlag(true);
        if(StringUtils.isEmpty(spuId)){
            BaseResponse<GoodsInfoByIdResponse> goodsInfo = goodsInfoQueryProvider.getById(GoodsInfoByIdRequest.builder().goodsInfoId(skuId).build());
            if(goodsInfo == null || goodsInfo.getContext() == null){
                throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
            }
            request.setGoodsId(goodsInfo.getContext().getGoodsId());
        }
        GoodsViewByIdResponse response = goodsQueryProvider.getViewById(request).getContext();
        return BaseResponse.success(KsBeanUtil.convert(response,GoodsDetailResponse.class));
    }

}
