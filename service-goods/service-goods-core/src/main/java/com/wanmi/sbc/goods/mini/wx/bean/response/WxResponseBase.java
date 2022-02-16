package com.wanmi.sbc.goods.mini.wx.bean.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WxResponseBase {

    @JsonProperty("errcode")
    private Integer errcode;

    @JsonProperty("errmsg")
    private String errmsg;

    public boolean isSuccess(){
        return errcode.equals(0);
    }
}
