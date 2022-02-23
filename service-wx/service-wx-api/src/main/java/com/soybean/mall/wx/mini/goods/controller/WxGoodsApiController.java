package com.soybean.mall.wx.mini.goods.controller;

import com.soybean.mall.wx.mini.goods.bean.request.WxAddProductRequest;
import com.soybean.mall.wx.mini.goods.bean.request.WxDeleteProductRequest;
import com.soybean.mall.wx.mini.goods.bean.request.WxUpdateProductWithoutAuditRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("/wx/mini")
@FeignClient(value = "${application.wx.name}", contextId = "WxMiniApiController")
public interface WxGoodsApiController {

    @PostMapping("/goods/add")
    BaseResponse<Boolean> addGoods(@RequestBody WxAddProductRequest wxAddProductRequest);

    @PostMapping("/goods/delete")
    BaseResponse<Boolean> deleteGoods(@RequestBody WxDeleteProductRequest wxDeleteProductRequest);

    @PostMapping("/goods/update-without-audit")
    BaseResponse<Boolean> updateGoodsWithoutAudit(@RequestBody WxUpdateProductWithoutAuditRequest wxUpdateProductWithoutAuditRequest);

    @PostMapping("/goods/verify/callback")
    BaseResponse verifyCallback(@RequestBody Map<String, String[]> parameterMap);
}
