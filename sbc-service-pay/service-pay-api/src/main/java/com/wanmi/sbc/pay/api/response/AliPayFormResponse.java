package com.wanmi.sbc.pay.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: service-pay
 * @description: 支付宝支付返回参数
 * @create: 2019-01-28 17:40
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliPayFormResponse implements Serializable {
    //支付宝支付表单
    private String form;
}
