package com.soybean.mall.wx.mini.bean.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WxGetOPenIdResponse extends WxResponseBase {

    private String openid;
    @JsonProperty("session_key")
    private String sessionKey;
    private String unionid;
}
