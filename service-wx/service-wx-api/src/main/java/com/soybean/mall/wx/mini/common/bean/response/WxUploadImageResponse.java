package com.soybean.mall.wx.mini.common.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import lombok.Data;

@Data
public class WxUploadImageResponse extends WxResponseBase {

    @JSONField(name = "img_info")
    private imgInfo imgInfo;

    @Data
    public static class imgInfo{

        @JSONField(name = "media_id")
        private String mediaId;
        @JSONField(name = "temp_img_url")
        private String tempImgUrl;
    }
}
