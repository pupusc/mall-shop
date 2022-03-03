package com.soybean.mall.wx.mini.goods.controller;

import com.soybean.mall.wx.mini.common.bean.request.WxUploadImageRequest;
import com.soybean.mall.wx.mini.common.bean.response.WxUploadImageResponse;
import com.soybean.mall.wx.mini.goods.bean.request.WxAddProductRequest;
import com.soybean.mall.wx.mini.goods.bean.request.WxDeleteProductRequest;
import com.soybean.mall.wx.mini.goods.bean.request.WxUpdateProductWithoutAuditRequest;
import com.soybean.mall.wx.mini.goods.bean.response.WxAddProductResponse;
import com.soybean.mall.wx.mini.goods.bean.response.WxCateNodeResponse;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import com.soybean.mall.wx.mini.service.WxService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.ShaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@Slf4j
public class WxGoodsApiControllerImpl implements WxGoodsApiController {

    @Autowired
    private WxService wxService;

    @Override
    public BaseResponse<WxAddProductResponse> addGoods(WxAddProductRequest wxAddProductRequest) {
        return BaseResponse.success(wxService.uploadGoodsToWx(wxAddProductRequest));
    }

    @Override
    public BaseResponse<WxAddProductResponse> updateGoods(WxAddProductRequest wxAddProductRequest) {
        return BaseResponse.success(wxService.updateGoods(wxAddProductRequest));
    }

    @Override
    public BaseResponse<Set<WxCateNodeResponse>> getAllCate() {
        return BaseResponse.success(wxService.getAllCate());
    }

    @Override
    public BaseResponse<WxResponseBase> cancelAudit(String goodsId) {
        return BaseResponse.success(wxService.cancelAudit(goodsId));
    }

    @Override
    public BaseResponse<Boolean> deleteGoods(@RequestBody WxDeleteProductRequest wxDeleteProductRequest){
        return BaseResponse.success(wxService.deleteGoods(wxDeleteProductRequest));
    }

    @Override
    public BaseResponse<WxResponseBase> updateGoodsWithoutAudit(@RequestBody WxUpdateProductWithoutAuditRequest wxUpdateProductWithoutAuditRequest){
        return BaseResponse.success(wxService.updateGoodsWithoutAudit(wxUpdateProductWithoutAuditRequest));
    }

    @Override
    public BaseResponse<WxUploadImageResponse> uploadImg(WxUploadImageRequest wxUploadImageRequest){
        return BaseResponse.success(wxService.uploadImg(wxUploadImageRequest));
    }

    //接入回调验证
    @Override
    public BaseResponse verifyCallback(Map<String, String[]> parameterMap) {
        String signature = parameterMap.get("signature")[0];
        String timestamp = parameterMap.get("timestamp")[0];
        String nonce = parameterMap.get("nonce")[0];
        String echostr = parameterMap.get("echostr")[0];
        String token = wxService.getAccessToken();
        List<String> arrays = Arrays.asList(token, timestamp, nonce);
        Collections.sort(arrays);
        String encryptSHA1 = ShaUtil.encryptSHA1(arrays.get(0).concat(arrays.get(1).concat(arrays.get(2))));
        log.info("接入回调sha1加密结果:{}", encryptSHA1);
        if(signature.equals(encryptSHA1)){
            log.info("接入回调验证成功");
            return BaseResponse.success(echostr);
        }
        log.info("接入回调验证失败");
        return BaseResponse.success(echostr);
    }
}
