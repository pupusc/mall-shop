package com.wanmi.sbc.callback.controller;

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

    @PostMapping(value = "/callback")
    public String goodsAuditCallback(HttpServletRequest request) throws IOException {
        log.info("微信商品回调: {}", IOUtils.toString(request.getInputStream()));
        try {
            request.getInputStream().reset();
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
            sb.append(k).append("=").append(v[0]).append("\n");
        });
        log.info("微信回调验证参数:{}", sb.toString());
        BaseResponse baseResponse = wxGoodsApiController.verifyCallback(parameterMap);
        return (String) baseResponse.getContext();
    }
}
