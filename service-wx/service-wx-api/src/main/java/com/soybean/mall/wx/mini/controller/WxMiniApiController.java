package com.soybean.mall.wx.mini.controller;

import com.soybean.mall.wx.mini.bean.request.WxAddProductRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/wx/mini")
@FeignClient(value = "${spring.application.name}", contextId = "WxMiniApiController")
public interface WxMiniApiController {

    @PostMapping("/goods/add")
    BaseResponse addGoods(@RequestBody WxAddProductRequest wxAddProductRequest);

    @PostMapping("/goods/audit/callback")
    BaseResponse auditCallback(HttpServletRequest httpServletRequest);

    @PostMapping("/goods/verify/callback")
    BaseResponse verifyCallback(HttpServletRequest request);
}
