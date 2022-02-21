package com.soybean.mall.wx.mini.goods.controller;

import com.soybean.mall.wx.mini.goods.bean.request.WxAddProductRequest;
import com.soybean.mall.wx.mini.goods.bean.request.WxDeleteProductRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/wx/mini")
@FeignClient(value = "${spring.application.name}", contextId = "WxMiniApiController")
public interface WxGoodsApiController {

    @PostMapping("/goods/add")
    BaseResponse<Boolean> addGoods(@RequestBody WxAddProductRequest wxAddProductRequest);

    @PostMapping("/goods/delete")
    BaseResponse<Boolean> deleteGoods(@RequestBody WxDeleteProductRequest wxDeleteProductRequest);

    @PostMapping("/goods/verify/callback")
    BaseResponse verifyCallback(HttpServletRequest request);
}
