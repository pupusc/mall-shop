package com.soybean.mall.wx.mini.user.bean.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WxGetUserPhoneAndOpenIdRequest {



    @NotBlank
    private String codeForOpenid;


    //登陆1
    private String encryptedData;

    private String iv;

    //登陆2
    private String codeForPhone;
}
