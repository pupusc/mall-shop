package com.soybean.mall.wx.mini.common.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;

import java.io.Serializable;

@Data
public class WxSendMessageRequest implements Serializable {
    private static final long serialVersionUID = 8673659265706744131L;

    /**
     * openid
     */
    @JSONField(name="touser")
    private String openId;

    /**
     * 所需下发的订阅模板id
     */
    @JSONField(name="template_id")
    private String templateId;

    /**
     * 点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
     */
    @JSONField(name="page")
    private String url;

    /**
     * 模板内容，格式形如 { "key1": { "value": any }, "key2": { "value": any } }
     */
    private Object data;


}
