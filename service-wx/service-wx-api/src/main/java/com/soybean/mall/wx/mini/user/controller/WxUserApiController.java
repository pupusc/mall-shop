package com.soybean.mall.wx.mini.user.controller;

import com.soybean.mall.wx.mini.user.bean.request.WxGetUserPhoneAndOpenIdRequest;
import com.soybean.mall.wx.mini.user.bean.response.WxGetUserPhoneAndOpenIdResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.wx.name}", contextId = "WxUserApiController")
public interface WxUserApiController {

    @PostMapping("/wx/mini/user/getPhoneAndOpenid")
    BaseResponse<WxGetUserPhoneAndOpenIdResponse> getPhoneAndOpenid(@RequestBody WxGetUserPhoneAndOpenIdRequest wxGetUserPhoneAndOpenIdRequest);
}
