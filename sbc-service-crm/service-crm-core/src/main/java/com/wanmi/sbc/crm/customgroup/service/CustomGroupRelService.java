package com.wanmi.sbc.crm.customgroup.service;

import com.wanmi.sbc.crm.api.request.crmgroup.CrmGroupRequest;
import com.wanmi.sbc.crm.customgroup.mapper.CustomGroupRelMapper;
import com.wanmi.sbc.crm.customgroup.model.CustomGroupRel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-15
 * \* Time: 14:13
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Service
public class CustomGroupRelService {

    @Autowired
    private CustomGroupRelMapper customGroupCustomerRelMapper;

    public List<CustomGroupRel> queryListByCustomerId(String customerId){
        return this.customGroupCustomerRelMapper.queryListByCustomerId(customerId);
    }

    public List<String> queryListByCustomerId(CrmGroupRequest request){
        return this.customGroupCustomerRelMapper.queryListByGroupId(request);
    }
}
