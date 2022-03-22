package com.soybean.mall.wx.mini.goods.bean.response;

import lombok.Data;

@Data
public class WxResponseBase {

    private Integer errcode;

    private String errmsg;

    public boolean isSuccess(){
        if(errcode == null) return true;
        return errcode.equals(0);
    }
}
