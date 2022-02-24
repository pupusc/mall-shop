package com.soybean.mall.wx.mini.goods.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WxGetPhoneNumberResponse extends WxResponseBase {

    @JSONField(name = "phone_info")
    private PhoneInfo phoneInfo;

    @Data
    public static class PhoneInfo{
        private String phoneNumber;
        private String purePhoneNumber;
        private String countryCode;
    }
}
