package com.wanmi.sbc.customer.provider.impl.fandeng;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.request.fandeng.*;
import com.wanmi.sbc.customer.api.response.customer.NoDeleteCustomerGetByAccountResponse;
import com.wanmi.sbc.customer.api.response.fandeng.*;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.fandeng.ExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: sbc-background
 * @description: 调用樊登接口层
 * @author: Mr.Tian
 * @create: 2021-01-28 15:24
 **/
@RestController
@Validated
public class ExternalController implements ExternalProvider {

    @Autowired
    private ExternalService externalService;

    @Override
    public BaseResponse<FanDengLoginResponse> loginConfirm(@Valid FanDengLoginRequest request) {
        return externalService.loginConfirm(request);
    }

    @Override
    public BaseResponse<FanDengLoginResponse> authLogin(@Valid FanDengAuthLoginRequest request) {
        return externalService.authLogin(request);
    }

    @Override
    public BaseResponse<FanDengPrepositionResponse> preposition( @Valid FanDengPrepositionRequest request) {
        return externalService.preposition(request);
    }

    @Override
    public BaseResponse<FanDengVerifyResponse> verify( @Valid FanDengVerifyRequest request) {
        return externalService.verify(request);
    }

    @Override
    public BaseResponse<FanDengSengCodeResponse> sendCode(@Valid FanDengSendCodeRequest request) {
        return externalService.sendCode(request);
    }

    @Override
    public BaseResponse<FanDengPointResponse> getByUserNoPoint(@Valid FanDengPointRequest request) {
        return externalService.getByUserNoPoint(request);
    }

    @Override
    public BaseResponse<FanDengLockResponse> pointLock(@Valid FanDengPointLockRequest request) {
        return externalService.pointLock(request);
    }
    @Override
    public BaseResponse<FanDengLockResponse> knowledgeLock(@Valid FanDengKnowledgeLockRequest request) {
        return externalService.knowledgeLock(request);
    }
    @Override
    public BaseResponse<FanDengConsumeResponse> pointDeduct(@Valid FanDengPointDeductRequest request) {
        return externalService.pointDeduct(request);
    }
    @Override
    public BaseResponse<FanDengConsumeResponse> knowledgeDeduct(@Valid FanDengPointDeductRequest request) {
        return externalService.knowledgeDeduct(request);
    }

    @Override
    public BaseResponse<FanDengConsumeResponse> pointCancel(@Valid FanDengPointCancelRequest request) {
        return externalService.pointCancel(request);
    }
    @Override
    public BaseResponse<FanDengConsumeResponse> knowledgeCancel(@Valid FanDengPointCancelRequest request) {
        return externalService.knowledgeCancel(request);
    }
    @Override
    public BaseResponse<FanDengConsumeResponse> pointRefund(@Valid FanDengPointRefundRequest request) {
        return externalService.pointRefund(request);
    }
    @Override
    public BaseResponse<FanDengConsumeResponse>knowledgeRefund(@Valid FanDengKnowledgeRefundRequest request) {
        return externalService.knowledgeRefund(request);
    }
    @Override
    public BaseResponse<MaterialCheckResponse> materialCheck(@Valid MaterialCheckRequest request) throws Exception {
        return externalService.materialCheck(request);
    }

    @Override
    public BaseResponse<NoDeleteCustomerGetByAccountResponse> modifyCustomer(@Valid FanDengModifyCustomerRequest request) {
        CustomerVO customerVO = externalService.saveFanDengCustomer(request);
        return BaseResponse.success(KsBeanUtil.convert(customerVO,NoDeleteCustomerGetByAccountResponse.class));
    }

    @Override
    public BaseResponse modifyPaidCustomer(@Valid FanDengModifyPaidCustomerRequest request) {
        return externalService.savePaidCard(request);
    }

    @Override
    public BaseResponse modifyCustomerLoginTime(@Valid FanDengModifyCustomerLoginTimeRequest request) {
        return externalService.modifyCustomerLoginTime(request);
    }

    @Override
    public BaseResponse modifyCustomerNameAndImg(@Valid FanDengModifyCustomerLoginTimeRequest request) {
        return externalService.modifyCustomerNameAndImg(request);
    }

    @Override
    public BaseResponse modifyCustomerAccountFanDeng(@Valid FanDengModifyAccountFanDengRequest request) {
        return externalService.modifyCustomerAccountFanDeng(request);
    }
}
