package com.wanmi.sbc.callback.controller;

import com.soybean.mall.order.api.provider.order.MiniAppOrderProvider;
import com.soybean.mall.order.api.request.order.WxMiniProgramCallbackRequest;
import com.soybean.mall.wx.mini.goods.controller.WxGoodsApiController;
import com.wanmi.sbc.callback.parser.WxAuditCallbackParser;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.ShaUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/wx")
public class WxCallbackController {

    @Autowired
    private WxAuditCallbackParser wxAuditCallbackParser;
    @Autowired
    private WxGoodsApiController wxGoodsApiController;
    @Autowired
    private MiniAppOrderProvider miniAppOrderProvider;

    @PostMapping(value = "/callback")
    public String goodsAuditCallback(HttpServletRequest request) {
        Long callbackId = null;
        String encryptStr = "";
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            StringBuilder sb = new StringBuilder(128);
            parameterMap.forEach((k, v) -> {
                sb.append(k).append("=").append(v[0]).append("&");
            });
            encryptStr = IOUtils.toString(request.getInputStream());
            log.info("微信回调参数: {}\nbody: {}", sb.toString(), encryptStr);
            BaseResponse<Long> response = miniAppOrderProvider.addCallback(WxMiniProgramCallbackRequest.builder().param(sb.toString()).content(encryptStr).status(0).build());
            if(response.getContext() == null){
                return "fail";
            }
            callbackId = response.getContext();
            wxAuditCallbackParser.dealCallback(encryptStr, parameterMap.get("timestamp")[0], parameterMap.get("nonce")[0], parameterMap.get("msg_signature")[0]);
            miniAppOrderProvider.updateCallback(WxMiniProgramCallbackRequest.builder().id(callbackId).status(2).build());
        }catch (Exception e) {
            log.error("微信回调失败:\n" + encryptStr, e);
            if(callbackId != null) miniAppOrderProvider.updateCallback(WxMiniProgramCallbackRequest.builder().id(callbackId).status(1).build());
        }
        return "fail";
    }

    @GetMapping("/callback")
    public String verifyCallback(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuilder sb = new StringBuilder(128);
        parameterMap.forEach((k, v) -> {
            sb.append(k).append("=").append(v[0]).append("\n");
        });
        log.info("微信回调验证参数:{}", sb.toString());
        BaseResponse baseResponse = wxGoodsApiController.verifyCallback(parameterMap);
        return (String) baseResponse.getContext();
    }
}
