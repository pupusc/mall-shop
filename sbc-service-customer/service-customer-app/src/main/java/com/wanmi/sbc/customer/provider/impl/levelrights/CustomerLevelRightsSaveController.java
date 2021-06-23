package com.wanmi.sbc.customer.provider.impl.levelrights;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.levelrights.CustomerLevelRightsSaveProvider;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsAddRequest;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsDeleteRequest;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsModifyRequest;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsQueryRequest;
import com.wanmi.sbc.customer.api.response.levelrights.CustomerLevelRightsResponse;
import com.wanmi.sbc.customer.levelrights.model.root.CustomerLevelRights;
import com.wanmi.sbc.customer.levelrights.service.CustomerLevelRightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>会员等级权益表保存服务接口实现</p>
 */
@RestController
@Validated
public class CustomerLevelRightsSaveController implements CustomerLevelRightsSaveProvider {
    @Autowired
    private CustomerLevelRightsService customerLevelRightsService;

    @Override
    public BaseResponse<CustomerLevelRightsResponse> add(@RequestBody @Valid CustomerLevelRightsAddRequest customerLevelRightsAddRequest) {
        CustomerLevelRights customerLevelRights = new CustomerLevelRights();
        KsBeanUtil.copyPropertiesThird(customerLevelRightsAddRequest, customerLevelRights);
        return BaseResponse.success(new CustomerLevelRightsResponse(
                customerLevelRightsService.wrapperVo(customerLevelRightsService.add(customerLevelRights))));
    }

    @Override
    public BaseResponse<CustomerLevelRightsResponse> modify(@RequestBody @Valid CustomerLevelRightsModifyRequest customerLevelRightsModifyRequest) {
        CustomerLevelRights customerLevelRights = new CustomerLevelRights();
        KsBeanUtil.copyPropertiesThird(customerLevelRightsModifyRequest, customerLevelRights);
        return BaseResponse.success(new CustomerLevelRightsResponse(
                customerLevelRightsService.wrapperVo(customerLevelRightsService.modify(customerLevelRights))));
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid CustomerLevelRightsDeleteRequest customerLevelRightsDeleteRequest) {
        customerLevelRightsService.deleteById(customerLevelRightsDeleteRequest.getRightsId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse editSort(@RequestBody CustomerLevelRightsQueryRequest queryRequest) {
        customerLevelRightsService.editSort(queryRequest);
        return BaseResponse.SUCCESSFUL();
    }

}

