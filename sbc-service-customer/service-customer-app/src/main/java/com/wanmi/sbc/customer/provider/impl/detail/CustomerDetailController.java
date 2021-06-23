package com.wanmi.sbc.customer.provider.impl.detail;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailProvider;
import com.wanmi.sbc.customer.api.request.detail.*;
import com.wanmi.sbc.customer.detail.model.root.CustomerDetail;
import com.wanmi.sbc.customer.detail.service.CustomerDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 会员明细-会员明细添加/修改/删除API实现
 *
 * @Author: daiyitian
 * @CreateDate: 2018/9/11 16:25
 * @Version: 1.0
 */
@RestController
@Validated
public class CustomerDetailController implements CustomerDetailProvider {

    @Autowired
    private CustomerDetailService customerDetailService;

    @Override

    public BaseResponse modifyCustomerStateByCustomerId(@RequestBody @Valid CustomerStateBatchModifyRequest request) {
        customerDetailService.updateCustomerState(request.getCustomerStatus(), request.getCustomerIds(), request
                .getForbidReason());
        return BaseResponse.SUCCESSFUL();
    }

    @Override

    public BaseResponse deleteCustomerDetailByCustomerIds(@RequestBody @Valid CustomerDetailDeleteRequest request) {
        customerDetailService.delete(request.getCustomerIds());
        return BaseResponse.SUCCESSFUL();
    }

    @Override

    public BaseResponse addCustomerDetail(@RequestBody @Valid CustomerDetailAddRequest request) {
        CustomerDetailEditRequest editRequest = new CustomerDetailEditRequest();
        KsBeanUtil.copyPropertiesThird(request, editRequest);
        customerDetailService.save(editRequest, request.getCreatePerson());
        return BaseResponse.SUCCESSFUL();
    }

    @Override

    public BaseResponse modifyCustomerDetail(@RequestBody @Valid CustomerDetailModifyRequest request) {
        CustomerDetail detail = new CustomerDetail();
        KsBeanUtil.copyPropertiesThird(request, detail);
        customerDetailService.update(detail);
        return BaseResponse.SUCCESSFUL();
    }
}
