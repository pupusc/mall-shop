package com.wanmi.sbc.customer.provider.impl.level;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelProvider;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelAddRequest;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelDeleteByIdRequest;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelEditRequest;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelModifyRequest;
import com.wanmi.sbc.customer.level.service.CustomerLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 会员等级-会员等级添加/修改/删除API
 *
 * @Author: daiyitian
 * @CreateDate: 2018/9/11 16:25
 * @Version: 1.0
 */
@RestController
@Validated
public class CustomerLevelController implements CustomerLevelProvider {

    @Autowired
    private CustomerLevelService customerLevelService;

    @Override

    public BaseResponse addCustomerLevel(@RequestBody @Valid CustomerLevelAddRequest request) {
        CustomerLevelEditRequest editRequest = new CustomerLevelEditRequest();
        KsBeanUtil.copyPropertiesThird(request, editRequest);
        customerLevelService.add(editRequest, request.getEmployeeId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override

    public BaseResponse modifyCustomerLevel(@RequestBody @Valid CustomerLevelModifyRequest request) {
        CustomerLevelEditRequest editRequest = new CustomerLevelEditRequest();
        KsBeanUtil.copyPropertiesThird(request, editRequest);
        customerLevelService.edit(editRequest, request.getEmployeeId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override

    public BaseResponse deleteCustomerLevel(@RequestBody @Valid CustomerLevelDeleteByIdRequest request) {
        if (customerLevelService.delete(request.getCustomerLevelId())) {
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.FAILED();
    }

}
