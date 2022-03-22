package com.soybean.mall.wx.mini.common.controller;

import com.soybean.mall.wx.mini.common.bean.request.WxSendMessageRequest;
import com.soybean.mall.wx.mini.common.bean.request.WxUploadImageRequest;
import com.soybean.mall.wx.mini.common.bean.response.WxUploadImageResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/wx/mini")
@FeignClient(value = "${application.wx.name}", contextId = "CommonController")
public interface CommonController {

    @PostMapping("/common/uploadImg")
    BaseResponse<WxUploadImageResponse> uploadImg(WxUploadImageRequest wxUploadImageRequest);

    @PostMapping("/common/send/msg")
    BaseResponse sendMessage(@RequestBody WxSendMessageRequest request);

}
