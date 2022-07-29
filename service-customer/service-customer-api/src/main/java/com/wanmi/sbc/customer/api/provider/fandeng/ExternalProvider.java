package com.wanmi.sbc.customer.api.provider.fandeng;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.fandeng.*;
import com.wanmi.sbc.customer.api.response.customer.NoDeleteCustomerGetByAccountResponse;
import com.wanmi.sbc.customer.api.response.fandeng.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "${application.customer.name}", contextId = "FanDengProvider")
public interface ExternalProvider {

    /**
     * 调用樊登免登接口
     *
     * @author tianzhengxin
     * @param request
     * @return  {@link FanDengLoginRequest}
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/login/confirm")
    BaseResponse<FanDengLoginResponse> loginConfirm(@RequestBody @Valid FanDengLoginRequest request);

    /**
     * 樊登登陆及注册接口共用
     *
     * @author tianzhengxin
     * @param request
     * @return  {@link FanDengAuthLoginRequest}
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/auth-login")
    BaseResponse<FanDengLoginResponse> authLogin(@RequestBody @Valid FanDengAuthLoginRequest request);

    /**
     * 微信授权登录
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/wx-auth-login")
    BaseResponse<FanDengWxAuthLoginResponse.WxAuthLoginData> wxAuthLogin(@RequestBody FanDengWxAuthLoginRequest request);

    /**
     * 调用樊登发送验证码前置接口
     *
     * @author tianzhengxin
     * @param request
     * @return  {@link FanDengPrepositionRequest}
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/preposition")
    BaseResponse<FanDengPrepositionResponse> preposition(@RequestBody @Valid FanDengPrepositionRequest request);


    /**
     * 调用樊登极验API
     *
     * @author tianzhengxin
     * @param request
     * @return  {@link FanDengVerifyRequest}
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/verify")
    BaseResponse<FanDengVerifyResponse> verify(@RequestBody @Valid FanDengVerifyRequest request);

    /**
     * 获取樊登验证码
     *
     * @author tianzhengxin
     * @param request
     * @return  {@link FanDengSendCodeRequest}
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/send-code")
    BaseResponse<FanDengSengCodeResponse> sendCode(@RequestBody @Valid FanDengSendCodeRequest request);



    /**
     * 通过樊登用户编号，查询樊登用户积分余额
     *
     * @author tianzhengxin
     * @param request
     * @return  {@link FanDengPointRequest}
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/get-by-user-no-point")
    BaseResponse<FanDengPointResponse> getByUserNoPoint(@RequestBody @Valid FanDengPointRequest request);


    /**
     * 通过樊登用户编号 查询樊登用户知豆余额
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/get-knowledge-by-fandeng-no")
    BaseResponse<FanDengKnowledgeResponse> getKnowledgeByFanDengNo(@RequestBody @Valid FanDengKnowledgeRequest request);


    /**
     * 樊登积分锁定接口
     *
     * @author tianzhengxin
     * @param request
     * @return  {@link FanDengPointLockRequest}
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/point-lock")
    BaseResponse<FanDengLockResponse> pointLock(@RequestBody @Valid FanDengPointLockRequest request);

    /**
     * 樊登知豆锁定接口
     *
     * @author tianzhengxin
     * @param request
     * @return  {@link FanDengPointLockRequest}
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/knowledge-lock")
    BaseResponse<FanDengLockResponse> knowledgeLock(@RequestBody @Valid FanDengKnowledgeLockRequest request);


    /**
     * 樊登扣除积分接口
     *
     * @author tianzhengxin
     * @param request
     * @return  {@link FanDengPointDeductRequest}
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/point-deduct")
    BaseResponse<FanDengConsumeResponse> pointDeduct(@RequestBody @Valid FanDengPointDeductRequest request);

    /**
     * 樊登扣除知豆接口
     *
     * @author tianzhengxin
     * @param request
     * @return  {@link FanDengPointDeductRequest}
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/knowledge-deduct")
    BaseResponse<FanDengConsumeResponse> knowledgeDeduct(@RequestBody @Valid FanDengPointDeductRequest request);


    /**
     * 樊登冻结积分返还积分接口
     *
     * @author tianzhengxin
     * @param request
     * @return  {@link FanDengPointCancelRequest}
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/point-cancel")
    BaseResponse<FanDengConsumeResponse> pointCancel(@RequestBody @Valid FanDengPointCancelRequest request);

    /**
     * 樊登冻结知豆返还知豆接口
     *
     * @author tianzhengxin
     * @param request
     * @return  {@link FanDengPointCancelRequest}
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/knowledge-cancel")
    BaseResponse<FanDengConsumeResponse> knowledgeCancel(@RequestBody @Valid FanDengPointCancelRequest request);

    /**
     * 退单积分退还接口
     *
     * @author tianzhengxin
     * @param request
     * @return  {@link FanDengPointRefundRequest}
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/point-refund")
    BaseResponse<FanDengConsumeResponse> pointRefund(@RequestBody @Valid FanDengPointRefundRequest request);
    /**
     * 退单知豆退还接口
     *
     * @author tianzhengxin
     * @param request
     * @return  {@link FanDengPointRefundRequest}
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/knowledge-refund")
    BaseResponse<FanDengConsumeResponse> knowledgeRefund(@RequestBody @Valid FanDengKnowledgeRefundRequest request);

    /**
     * 获取樊登素材审核接口 跟签名参数
     *
     * @author tianzhengxin
     * @param request
     * @return  {@link FanDengPointRefundRequest}
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/material-check")
    BaseResponse<MaterialCheckResponse> materialCheck(@RequestBody @Valid MaterialCheckRequest request) throws Exception;


    /**
     * 樊登登录新增用户
     *
     * @author tianzhengxin
     * @param request
     * @return  {@link FanDengPointRefundRequest}
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/modify-customer")
    BaseResponse<NoDeleteCustomerGetByAccountResponse> modifyCustomer(@RequestBody @Valid FanDengModifyCustomerRequest request);
    /**
     * 樊登登录修改付费会员状态
     *
     * @author tianzhengxin
     * @param request
     * @return  {@link FanDengPointRefundRequest}
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/modify-paid-customer")
    BaseResponse modifyPaidCustomer(@RequestBody @Valid FanDengModifyPaidCustomerRequest request);

        /**
     * 修改登录时间
     *
     * @author tianzhengxin
     * @param request
     * @return  {@link FanDengPointRefundRequest}
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/modify-customer-login-time")
    BaseResponse modifyCustomerLoginTime(@RequestBody @Valid FanDengModifyCustomerLoginTimeRequest request);

    /**
     * 修改登录之后的图片跟名称
     *
     * @author tianzhengxin
     * @param request
     * @return  {@link FanDengPointRefundRequest}
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/modify-customer-name-img")
    BaseResponse modifyCustomerNameAndImg(@RequestBody @Valid FanDengModifyCustomerLoginTimeRequest request);

   /**
     * 修改登录时间
     *
     * @author tianzhengxin
     * @param request
     * @return  {@link FanDengPointRefundRequest}
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/modify-customer-account-fandeng")
    BaseResponse modifyCustomerAccountFanDeng(@RequestBody @Valid FanDengModifyAccountFanDengRequest request);


    /**
     * 提交订单开票
     *
     * @author lancey
     * @param request
     * @return  开票提交的key
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/submit/invoice/order")
    BaseResponse<String> submitInvoiceOrder(@RequestBody @Valid FanDengInvoiceRequest request);


    /**
     * 直接开票，需要包含发票相关的信息
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/submit/invoice/full")
    BaseResponse<String> createInvoice(@RequestBody @Valid FanDengFullInvoiceRequest request);


    /**
     * 变更积分
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/fan-deng/change-point")
    BaseResponse changePoint(@RequestBody @Validated FanDengAddPointReq request);
}
