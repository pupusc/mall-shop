package com.soybean.mall.wx.mini.user.bean.response;

import lombok.Data;

@Data
public class WxGetUserPhoneAndOpenIdResponse {

    private String phoneNumber;
    private String openId;
    private String unionId;
}
