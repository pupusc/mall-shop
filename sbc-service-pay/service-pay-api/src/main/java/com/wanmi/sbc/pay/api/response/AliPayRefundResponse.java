package com.wanmi.sbc.pay.api.response;

import com.alipay.api.response.AlipayTradeRefundResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: service-pay
 * @description: 支付宝退款返回参数
 * @create: 2019-02-15 10:47
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliPayRefundResponse implements Serializable {
    //退款接口返回的参数
    private AlipayTradeRefundResponse alipayTradeRefundResponse;
    //
    private String payType;
}
