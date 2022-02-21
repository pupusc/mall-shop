package com.soybean.mall.wx.mini.goods.bean.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WxGetPhoneNumberResponse extends WxResponseBase {

    @JsonProperty("phone_info")
    private PhoneInfo phoneInfo;

    @Data
    public static class PhoneInfo{
        private String phoneNumber;
        private String purePhoneNumber;
        private String countryCode;
    }
}
