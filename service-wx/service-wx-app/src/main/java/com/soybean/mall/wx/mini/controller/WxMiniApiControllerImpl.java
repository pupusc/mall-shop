package com.soybean.mall.wx.mini.controller;

import com.soybean.mall.wx.mini.bean.request.WxAddProductRequest;
import com.soybean.mall.wx.mini.callback.parser.WxAuditCallbackParser;
import com.soybean.mall.wx.mini.service.WxService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.ShaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
public class WxMiniApiControllerImpl implements WxMiniApiController {

    @Autowired
    private WxService wxService;
    @Autowired
    private WxAuditCallbackParser wxAuditCallbackParser;

    @Override
    public BaseResponse addGoods(WxAddProductRequest wxAddProductRequest) {
        wxService.uploadGoodsToWx(wxAddProductRequest);
        return BaseResponse.SUCCESSFUL();
    }

    //审核事件回调
    @Override
    public BaseResponse auditCallback(HttpServletRequest request) {
//        wxAuditCallbackParser.dealCallback(new ByteArrayInputStream(callbackStr.getBytes()));
        try {
            wxAuditCallbackParser.dealCallback(request.getInputStream());
        } catch (IOException e) {
            log.info("微信商品审核回调失败", e);
        }
        return null;
    }

    //接入回调验证
    @Override
    public BaseResponse verifyCallback(HttpServletRequest request) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
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
