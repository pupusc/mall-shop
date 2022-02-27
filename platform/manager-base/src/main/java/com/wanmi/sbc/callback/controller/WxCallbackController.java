package com.wanmi.sbc.callback.controller;

import com.soybean.mall.wx.mini.goods.controller.WxGoodsApiController;
import com.wanmi.sbc.callback.parser.WxAuditCallbackParser;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.ShaUtil;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping(value = "/callback")
    public String goodsAuditCallback(HttpServletRequest request) {
        log.info("微信商品回调");
        try {
            return wxAuditCallbackParser.dealCallback(request.getInputStream());
        } catch (IOException e) {
            log.error("微信回调失败");
            return "fail";
        }
    }

    @GetMapping("/callback")
    public String verifyCallback(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuilder sb = new StringBuilder(128);
        parameterMap.forEach((k, v) -> {
            sb.append(k).append("=").append(v).append("\n");
        });
        log.info("微信回调验证参数:{}", sb.toString());
//        BaseResponse baseResponse = wxGoodsApiController.verifyCallback(parameterMap);
//        return (String) baseResponse.getContext();
        return verifyCallback(parameterMap);
    }

    private String verifyCallback(Map<String, String[]> parameterMap){
        String signature = parameterMap.get("signature")[0];
        String timestamp = parameterMap.get("timestamp")[0];
        String nonce = parameterMap.get("nonce")[0];
        String echostr = parameterMap.get("echostr")[0];
        String token = parameterMap.get("token")[0];
        List<String> arrays = Arrays.asList(token, timestamp, nonce);
        Collections.sort(arrays);
        String encryptSHA1 = ShaUtil.encryptSHA1(arrays.get(0).concat(arrays.get(1).concat(arrays.get(2))));
        log.info("接入回调sha1加密结果:{}", encryptSHA1);
        if(signature.equals(encryptSHA1)){
            return echostr;
        }
        return echostr;
    }
}
