package com.soybean.mall.wx.mini.goods.controller;

import com.soybean.mall.wx.mini.goods.bean.request.WxAddProductRequest;
import com.soybean.mall.wx.mini.goods.bean.request.WxDeleteProductRequest;
import com.soybean.mall.wx.mini.goods.bean.request.WxUpdateProductWithoutAuditRequest;
import com.soybean.mall.wx.mini.service.WxService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.ShaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class WxGoodsApiControllerImpl implements WxGoodsApiController {

    @Autowired
    private WxService wxService;

    @Override
    public BaseResponse<Boolean> addGoods(WxAddProductRequest wxAddProductRequest) {
        boolean b = wxService.uploadGoodsToWx(wxAddProductRequest);
        return BaseResponse.success(b);
    }

    @Override
    public BaseResponse<Boolean> deleteGoods(@RequestBody WxDeleteProductRequest wxDeleteProductRequest){
        return BaseResponse.success(wxService.deleteGoods(wxDeleteProductRequest));
    }

    @Override
    public BaseResponse<Boolean> updateGoodsWithoutAudit(@RequestBody WxUpdateProductWithoutAuditRequest wxUpdateProductWithoutAuditRequest){
        return BaseResponse.success(wxService.updateGoodsWithoutAudit(wxUpdateProductWithoutAuditRequest));
    }

    //接入回调验证
    @Override
    public BaseResponse verifyCallback(Map<String, String[]> parameterMap) {
        String signature = parameterMap.get("signature")[0];
        String timestamp = parameterMap.get("timestamp")[0];
        String nonce = parameterMap.get("nonce")[0];
        String echostr = parameterMap.get("echostr")[0];
        String token = wxService.getAccessToken();
        log.info("微信回调接入，参数:{},{},{},{},{}", signature, timestamp, nonce, echostr, token);
        List<String> arrays = Arrays.asList(token, timestamp, nonce);
        Collections.sort(arrays);
        String encryptSHA1 = ShaUtil.encryptSHA1(arrays.get(0).concat(arrays.get(1).concat(arrays.get(2))));
        log.info("接入回调sha1加密结果:{}", encryptSHA1);
        if(signature.equals(encryptSHA1)){
            log.info("接入回调成功");
            return BaseResponse.success(echostr);
        }
        log.info("接入回调失败");
        return BaseResponse.success("fail");
    }
}
