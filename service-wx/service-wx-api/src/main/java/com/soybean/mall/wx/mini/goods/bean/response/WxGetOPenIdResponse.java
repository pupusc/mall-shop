package com.soybean.mall.wx.mini.goods.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WxGetOPenIdResponse extends WxResponseBase {

    private String openid;
    @JSONField(name = "session_key")
    private String sessionKey;
    private String unionid;
}
