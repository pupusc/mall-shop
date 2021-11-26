package com.wanmi.sbc.crm.customerlevel.service;

import com.wanmi.sbc.crm.customerlevel.mapper.CustomerLevelMapper;
import com.wanmi.sbc.crm.customerlevel.model.CustomerLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-13
 * \* Time: 16:39
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Service
public class CustomerLevelService {
    @Autowired
    private CustomerLevelMapper customerLevelMapper;

    public List<CustomerLevel> queryList(CustomerLevel customerLevel){
        return this.customerLevelMapper.queryList(customerLevel);
    }

    public int queryCount(CustomerLevel customerLevel) {
        return this.customerLevelMapper.queryCount(customerLevel);
    }
}
