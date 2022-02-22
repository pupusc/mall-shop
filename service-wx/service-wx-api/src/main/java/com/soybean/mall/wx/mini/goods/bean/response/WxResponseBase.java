package com.soybean.mall.wx.mini.goods.bean.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WxResponseBase {

    @JsonProperty("errcode")
    private Integer errcode;

    @JsonProperty("errmsg")
    private String errmsg;

    public boolean isSuccess(){
        if(errcode == null) return true;
        return errcode.equals(0);
    }
}
