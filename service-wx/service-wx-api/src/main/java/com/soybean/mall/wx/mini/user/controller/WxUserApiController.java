package com.soybean.mall.wx.mini.user.controller;

import com.soybean.mall.wx.mini.user.bean.request.WxGetOpenIdReq;
import com.soybean.mall.wx.mini.user.bean.request.WxGetPhoneOldReq;
import com.soybean.mall.wx.mini.user.bean.request.WxGetPhoneReq;
import com.soybean.mall.wx.mini.user.bean.request.WxGetUserPhoneAndOpenIdRequest;
import com.soybean.mall.wx.mini.user.bean.response.WxGetUserPhoneAndOpenIdResponse;
import com.soybean.mall.wx.mini.user.bean.response.WxUserOpenIdResp;
import com.soybean.mall.wx.mini.user.bean.response.WxUserPhoneResp;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.wx.name}", contextId = "WxUserApiController")
public interface WxUserApiController {

    @PostMapping("/wx/mini/user/getPhoneNumberOld")
    BaseResponse<WxUserPhoneResp> getPhoneNumberOld(@RequestBody @Validated WxGetPhoneOldReq request);

    /**
     * 获取电话信息
     * @param request
     * @return
     */
    @PostMapping("/wx/mini/user/getPhoneNumber")
    BaseResponse<WxUserPhoneResp> getPhoneNumber(@RequestBody @Validated WxGetPhoneReq request);


    /**
     * 获取openId
     * @param request
     * @return
     */
    @PostMapping("/wx/mini/user/getOpenid")
    BaseResponse<WxUserOpenIdResp> getOpenid(@RequestBody @Validated WxGetOpenIdReq request);



//    @PostMapping("/wx/mini/user/getPhoneAndOpenid")
//    BaseResponse<WxGetUserPhoneAndOpenIdResponse> getPhoneAndOpenid(@RequestBody WxGetUserPhoneAndOpenIdRequest wxGetUserPhoneAndOpenIdRequest);
}
