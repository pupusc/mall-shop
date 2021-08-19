package com.wanmi.sbc.crm.crmgroup;

import com.github.pagehelper.PageHelper;
import com.wanmi.sbc.crm.api.request.crmgroup.CrmGroupRequest;
import com.wanmi.sbc.crm.customgroup.mapper.CustomGroupMapper;
import com.wanmi.sbc.crm.customgroup.mapper.CustomGroupRelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-12-9
 * \* Time: 16:18
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Service
public class CrmGroupService {
    @Autowired
    private CustomGroupRelMapper customGroupRelMapper;

    @Autowired
    private CustomGroupMapper customGroupMapper;

    public List<String> queryCustomerPhone(CrmGroupRequest request){
        PageHelper.startPage(request.getPageNum()+1,request.getPageSize(),false);
        List<String> phoneNumbers = this.customGroupRelMapper.queryCustomerPhone(request);
        return phoneNumbers;
    }

    public long queryCustomerPhoneCount(CrmGroupRequest request){
        return this.customGroupRelMapper.queryCustomerPhoneCount(request);

    }

    public List<String> queryListByGroupId(CrmGroupRequest request){
        PageHelper.startPage(request.getPageNum()+1,request.getPageSize(),false);
        return this.customGroupRelMapper.queryListByGroupId(request);
    }

    public Long countByGroupIds(CrmGroupRequest request){
        return this.customGroupMapper.countByGroupIds(request);
    }
}
