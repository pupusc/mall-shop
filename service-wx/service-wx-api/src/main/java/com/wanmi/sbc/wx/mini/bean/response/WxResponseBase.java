package com.wanmi.sbc.wx.mini.bean.response;

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
