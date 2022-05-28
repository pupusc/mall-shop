package com.soybean.mall.wx.mini.user;

import com.soybean.mall.wx.mini.goods.bean.response.WxGetOPenIdResponse;
import com.soybean.mall.wx.mini.service.WxService;
import com.soybean.mall.wx.mini.user.bean.request.WxGetUserPhoneAndOpenIdRequest;
import com.soybean.mall.wx.mini.user.bean.response.WxGetUserPhoneAndOpenIdResponse;
import com.soybean.mall.wx.mini.user.controller.WxUserApiController;
import com.wanmi.sbc.common.base.BaseResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WxUserApiControllerImpl implements WxUserApiController {

    @Autowired
    private WxService wxService;

    @Override
    public BaseResponse<WxGetUserPhoneAndOpenIdResponse> getPhoneAndOpenid(WxGetUserPhoneAndOpenIdRequest wxGetUserPhoneAndOpenIdRequest) {
        WxGetUserPhoneAndOpenIdResponse wxGetUserPhoneAndOpenIdResponse = new WxGetUserPhoneAndOpenIdResponse();
        if(!StringUtils.isBlank(wxGetUserPhoneAndOpenIdRequest.getCodeForPhone())){
            String phoneNumber = wxService.getPhoneNumber(wxGetUserPhoneAndOpenIdRequest.getCodeForPhone());
            wxGetUserPhoneAndOpenIdResponse.setPhoneNumber(phoneNumber);
        }
        if(!StringUtils.isBlank(wxGetUserPhoneAndOpenIdRequest.getCodeForOpenid())){
            WxGetOPenIdResponse getOPenIdResponse = wxService.getOpenId(wxGetUserPhoneAndOpenIdRequest.getCodeForOpenid());
            wxGetUserPhoneAndOpenIdResponse.setOpenId(getOPenIdResponse.getOpenid());
            wxGetUserPhoneAndOpenIdResponse.setUnionId(getOPenIdResponse.getUnionid());
        }
        return BaseResponse.success(wxGetUserPhoneAndOpenIdResponse);
    }
}
