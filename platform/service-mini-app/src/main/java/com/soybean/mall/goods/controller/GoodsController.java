package com.soybean.mall.goods.controller;

import com.soybean.mall.goods.response.GoodsDetailResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsViewByIdRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsViewByIdResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("wxGoodsController")
@RequestMapping("/wx/goods")
public class GoodsController {

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    /**
     * @description 商品详情页
     * @param spuId
     * @menu 小程序
     * @status done
     */
    @GetMapping("/detail")
    public BaseResponse<GoodsDetailResponse> detail(@RequestParam("spuId")String spuId){
        GoodsViewByIdRequest request = new GoodsViewByIdRequest();
        request.setGoodsId(spuId);
        request.setShowLabelFlag(true);
        GoodsViewByIdResponse response = goodsQueryProvider.getViewById(request).getContext();
        return BaseResponse.success(KsBeanUtil.convert(response,GoodsDetailResponse.class));
    }

}
