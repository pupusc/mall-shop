package com.soybean.mall.wx.mini.user;

import com.soybean.mall.wx.mini.goods.bean.response.WxGetOPenIdResponse;
import com.soybean.mall.wx.mini.service.WxService;
import com.soybean.mall.wx.mini.user.bean.request.WxGetOpenIdReq;
import com.soybean.mall.wx.mini.user.bean.request.WxGetPhoneOldReq;
import com.soybean.mall.wx.mini.user.bean.request.WxGetPhoneReq;
import com.soybean.mall.wx.mini.user.bean.response.WxBizDataResp;
import com.soybean.mall.wx.mini.util.WXBizDataCrypt;
import com.soybean.mall.wx.mini.user.bean.response.WxUserOpenIdResp;
import com.soybean.mall.wx.mini.user.bean.response.WxUserPhoneResp;
import com.soybean.mall.wx.mini.user.controller.WxUserApiController;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WxUserApiControllerImpl implements WxUserApiController {

    @Autowired
    private WxService wxService;

    @Override
    public BaseResponse<WxUserPhoneResp> getPhoneNumberOld(WxGetPhoneOldReq request){
        //获取电话
        WxUserPhoneResp wxUserPhoneResp = new WxUserPhoneResp();
        WXBizDataCrypt wxBizDataCrypt = new WXBizDataCrypt(request.getSessionKey());
        WxBizDataResp wxBizDataResp = wxBizDataCrypt.decrypt(request.getEncryptedData(), request.getIv());
        if (wxBizDataResp != null) {
            wxUserPhoneResp.setPhoneNumber(wxBizDataResp.getPhoneNumber());
        }
        return BaseResponse.success(wxUserPhoneResp);
    }


    @Override
    public BaseResponse<WxUserPhoneResp> getPhoneNumber(WxGetPhoneReq request){
        //获取电话
        WxUserPhoneResp wxUserPhoneResp = new WxUserPhoneResp();
        String phoneNumber = "";
        if (StringUtils.isNotBlank(request.getCodeForPhone())) {
            phoneNumber = wxService.getPhoneNumber(request.getCodeForPhone());
            wxUserPhoneResp.setPhoneNumber(phoneNumber);
        }
        return BaseResponse.success(wxUserPhoneResp);
    }

    @Override
    public BaseResponse<WxUserOpenIdResp> getOpenid(WxGetOpenIdReq request) {
        WxUserOpenIdResp wxUserOpenIdResp = new WxUserOpenIdResp();
        if(!StringUtils.isBlank(request.getCodeForOpenid())){
            WxGetOPenIdResponse getOPenIdResponse = wxService.getOpenId(request.getCodeForOpenid());
            wxUserOpenIdResp.setOpenId(getOPenIdResponse.getOpenid());
            wxUserOpenIdResp.setUnionId(getOPenIdResponse.getUnionid());
            wxUserOpenIdResp.setSessionKey(getOPenIdResponse.getSessionKey());
        }
        return BaseResponse.success(wxUserOpenIdResp);
    }

//    @Override
//    public BaseResponse<WxGetUserPhoneAndOpenIdResponse> getPhoneAndOpenid(WxGetUserPhoneAndOpenIdRequest wxGetUserPhoneAndOpenIdRequest) {
//        WxGetUserPhoneAndOpenIdResponse wxGetUserPhoneAndOpenIdResponse = new WxGetUserPhoneAndOpenIdResponse();
//        if(!StringUtils.isBlank(wxGetUserPhoneAndOpenIdRequest.getCodeForPhone())){
//            String phoneNumber = wxService.getPhoneNumber(wxGetUserPhoneAndOpenIdRequest.getCodeForPhone());
//            wxGetUserPhoneAndOpenIdResponse.setPhoneNumber(phoneNumber);
//        }
//        if(!StringUtils.isBlank(wxGetUserPhoneAndOpenIdRequest.getCodeForOpenid())){
//            WxGetOPenIdResponse getOPenIdResponse = wxService.getOpenId(wxGetUserPhoneAndOpenIdRequest.getCodeForOpenid());
//            wxGetUserPhoneAndOpenIdResponse.setOpenId(getOPenIdResponse.getOpenid());
//            wxGetUserPhoneAndOpenIdResponse.setUnionId(getOPenIdResponse.getUnionid());
//        }
//        return BaseResponse.success(wxGetUserPhoneAndOpenIdResponse);
//    }
//
//
//    private void decrypt(String encryptedData, String iv) {
//        Base64.Decoder decoder = Base64.getDecoder();
//        byte[] encryptedDataByte = decoder.decode(encryptedData);
//        byte[] ivByte = decoder.decode(iv);
//    }
}
