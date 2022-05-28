package com.soybean.mall.wx.mini.common;

import com.soybean.mall.wx.mini.common.bean.request.UrlschemeRequest;
import com.soybean.mall.wx.mini.common.bean.request.WxSendMessageRequest;
import com.soybean.mall.wx.mini.common.bean.request.WxUploadImageRequest;
import com.soybean.mall.wx.mini.common.bean.response.WxUploadImageResponse;
import com.soybean.mall.wx.mini.common.controller.CommonController;
import com.soybean.mall.wx.mini.service.WxService;
import com.wanmi.sbc.common.base.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CommonControllerImpl implements CommonController {

    @Autowired
    private WxService wxService;

    @Override
    public BaseResponse<WxUploadImageResponse> uploadImg(WxUploadImageRequest wxUploadImageRequest) {
        WxUploadImageResponse wxUploadImageResponse = wxService.uploadImg(wxUploadImageRequest);
        return BaseResponse.success(wxUploadImageResponse);
    }

    @Override
    public BaseResponse sendMessage(WxSendMessageRequest request) {
        return BaseResponse.success(wxService.sendMessage(request));
    }

    @Override
    public BaseResponse<String> urlschemeGenerate(UrlschemeRequest request) {
        return wxService.urlschemeGenerate(request);
    }

}
