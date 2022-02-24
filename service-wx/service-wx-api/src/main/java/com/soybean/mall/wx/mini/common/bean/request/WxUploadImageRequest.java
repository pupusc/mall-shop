package com.soybean.mall.wx.mini.common.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WxUploadImageRequest {

    @JSONField(name = "resp_type")
    private Integer respType;
    @JSONField(name = "upload_type")
    private Integer uploadType;
    @JSONField(name = "img_url")
    private String imgUrl;
}
