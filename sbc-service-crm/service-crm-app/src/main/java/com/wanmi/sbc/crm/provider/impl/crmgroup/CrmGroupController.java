package com.wanmi.sbc.crm.provider.impl.crmgroup;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.provider.crmgroup.CrmGroupProvider;
import com.wanmi.sbc.crm.api.request.crmgroup.CrmGroupRequest;
import com.wanmi.sbc.crm.api.request.customgrouprel.CustomerGroupRelPageRequest;
import com.wanmi.sbc.crm.crmgroup.CrmGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-15
 * \* Time: 14:50
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@RestController
public class CrmGroupController implements CrmGroupProvider {

    @Autowired
    private CrmGroupService crmGroupService;

    @Override
    public BaseResponse<List<String>> queryCustomerPhone(@RequestBody CrmGroupRequest request) {
        List<String> phoneNumbers = this.crmGroupService.queryCustomerPhone(request);
        return BaseResponse.success(phoneNumbers);
    }

    @Override
    public BaseResponse<Long> queryCustomerPhoneCount(@RequestBody CrmGroupRequest request) {
       return BaseResponse.success(this.crmGroupService.queryCustomerPhoneCount(request));

    }

    @Override
    public BaseResponse<List<String>> queryListByGroupId(@RequestBody CrmGroupRequest request) {
        List<String> customerIds = crmGroupService.queryListByGroupId(request);
        return BaseResponse.success(customerIds);
    }

    @Override
    public BaseResponse<Long> countByGroupIds(@RequestBody @Valid CrmGroupRequest request){
        return BaseResponse.success(crmGroupService.countByGroupIds(request));
    }
}
