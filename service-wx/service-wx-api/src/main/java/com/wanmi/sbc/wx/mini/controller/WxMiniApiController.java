package com.wanmi.sbc.wx.mini.controller;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.wx.mini.bean.request.WxAddProductRequest;
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
}
