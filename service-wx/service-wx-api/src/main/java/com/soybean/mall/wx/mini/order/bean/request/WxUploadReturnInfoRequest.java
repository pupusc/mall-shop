package com.soybean.mall.wx.mini.order.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;


@Data
public class WxUploadReturnInfoRequest implements Serializable {

    @JSONField(name = "out_aftersale_id")
    private String outAftersaleId;

    private String openid;

    /**
     * 快递公司code，eg:SF
     */
    @JSONField(name ="delivery_id")
    private String deliveryId;

    /**
     *单号
     */
    @JSONField(name ="waybill_id")
    private String wayBillId;

    /**
     * 快递公司名称
     */
    @JSONField(name ="delivery_name")
    private String deliveryName;
}