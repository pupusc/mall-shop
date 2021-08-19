package com.wanmi.sbc.pay.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.pay.api.request.AliPayRefundRequest;
import com.wanmi.sbc.pay.api.request.PayExtraRequest;
import com.wanmi.sbc.pay.api.response.AliPayFormResponse;
import com.wanmi.sbc.pay.api.response.AliPayRefundResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @program: service-pay
 * @description: 支付宝相关接口
 * @create: 2019-01-28 16:15
 **/
@FeignClient(value = "${application.pay.name}", contextId = "AliPayProvider")
public interface AliPayProvider {

    /*
     * @Description: 支付接口，返回支付宝支付表单前端自动提交重定向到支付宝收银台
     * @Param:  request 支付请求对象
     * @Author: Bob
     * @Date: 2019-01-28 16:25
    */
    @PostMapping("/pay/${application.pay.version}/get-payForm")
    BaseResponse<AliPayFormResponse> getPayForm(@RequestBody @Valid PayExtraRequest request);

    /*
     * @Description: 退款接口，直接退款，不涉及业务
     * @Param:  request 支付请求对象
     * @Author: Bob
     * @Date: 2019-01-28 16:25
     */
    @PostMapping("/pay/${application.pay.version}/aliPayRefund")
    BaseResponse<AliPayRefundResponse> aliPayRefund(@RequestBody @Valid AliPayRefundRequest refundRequest);

}
