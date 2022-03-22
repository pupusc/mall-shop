package com.soybean.mall.wx.mini.goods.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WxAccessTokenResponse extends WxResponseBase {

    @JSONField(name = "access_token")
    private String accessToken;

    @JSONField(name = "expires_in")
    private Long expiresIn;

}
