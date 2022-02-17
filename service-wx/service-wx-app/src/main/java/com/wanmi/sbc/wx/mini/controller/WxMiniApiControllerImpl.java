package com.wanmi.sbc.wx.mini.controller;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.wx.mini.bean.request.WxAddProductRequest;
import com.wanmi.sbc.wx.mini.callback.parser.WxAuditCallbackParser;
import com.wanmi.sbc.wx.mini.service.WxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
}
