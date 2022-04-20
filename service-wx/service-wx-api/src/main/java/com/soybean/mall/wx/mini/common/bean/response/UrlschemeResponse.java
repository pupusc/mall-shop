package com.soybean.mall.wx.mini.common.bean.response;

import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import lombok.Data;

@Data
public class UrlschemeResponse extends WxResponseBase {

    /**
     * 生成的链接
     */
    String openlink;
}
