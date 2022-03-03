package com.soybean.mall.wx.mini.goods.controller;

import com.soybean.mall.wx.mini.common.bean.request.WxUploadImageRequest;
import com.soybean.mall.wx.mini.common.bean.response.WxUploadImageResponse;
import com.soybean.mall.wx.mini.goods.bean.request.WxAddProductRequest;
import com.soybean.mall.wx.mini.goods.bean.request.WxDeleteProductRequest;
import com.soybean.mall.wx.mini.goods.bean.request.WxUpdateProductWithoutAuditRequest;
import com.soybean.mall.wx.mini.goods.bean.response.WxAddProductResponse;
import com.soybean.mall.wx.mini.goods.bean.response.WxCateNodeResponse;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

@RequestMapping("/wx/mini")
@FeignClient(value = "${application.wx.name}", contextId = "WxMiniApiController")
public interface WxGoodsApiController {

    @PostMapping("/goods/add")
    BaseResponse<WxAddProductResponse> addGoods(@RequestBody WxAddProductRequest wxAddProductRequest);

    @PostMapping("/goods/update")
    BaseResponse<WxAddProductResponse> updateGoods(@RequestBody WxAddProductRequest wxAddProductRequest);

    @PostMapping("/goods/cancel-audit")
    BaseResponse<WxResponseBase> cancelAudit(@RequestParam("goodsId") String goodsId);

    @PostMapping("/goods/get-all-cate")
    BaseResponse<Set<WxCateNodeResponse>> getAllCate();

    @PostMapping("/goods/delete")
    BaseResponse<Boolean> deleteGoods(@RequestBody WxDeleteProductRequest wxDeleteProductRequest);

    @PostMapping("/goods/update-without-audit")
    BaseResponse<WxResponseBase> updateGoodsWithoutAudit(@RequestBody WxUpdateProductWithoutAuditRequest wxUpdateProductWithoutAuditRequest);

    @PostMapping("/goods/upload-image")
    BaseResponse<WxUploadImageResponse> uploadImg(@RequestBody WxUploadImageRequest wxUploadImageRequest);

    @PostMapping("/goods/verify/callback")
    BaseResponse verifyCallback(@RequestBody Map<String, String[]> parameterMap);
}
