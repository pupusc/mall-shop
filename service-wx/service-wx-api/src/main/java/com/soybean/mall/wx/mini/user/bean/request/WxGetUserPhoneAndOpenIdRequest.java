package com.soybean.mall.wx.mini.user.bean.request;

import lombok.Data;

@Data
public class WxGetUserPhoneAndOpenIdRequest {

    private String codeForPhone;
    private String codeForOpenid;
}
