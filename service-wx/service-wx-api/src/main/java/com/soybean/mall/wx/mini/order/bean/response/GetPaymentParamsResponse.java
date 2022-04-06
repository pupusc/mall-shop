package com.soybean.mall.wx.mini.order.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import com.soybean.mall.wx.mini.order.bean.dto.PaymentParamsDTO;
import lombok.Data;


@Data
public class GetPaymentParamsResponse extends WxResponseBase {
    @JSONField(name="payment_params")
    private PaymentParamsDTO paymentParams;
}
