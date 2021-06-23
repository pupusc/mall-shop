package com.wanmi.ares.report.customer.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wanmi.ares.report.customer.dao.CustomerMapper;
import com.wanmi.ares.request.CustomerQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-12-6
 * \* Time: 11:13
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Service
public class CustomerQueryService {

    @Autowired
    private CustomerMapper customerMapper;

    public PageInfo<String> queryPhoneAccount(CustomerQueryRequest request){
        PageHelper.startPage(request.getPageNum().intValue()+1,request.getPageSize().intValue());
        List<String> list = this.customerMapper.queryPhone(request);
        PageInfo<String> pageInfo = new PageInfo<String>(list);
        return pageInfo;
    }

    public long queryPhoneCount(CustomerQueryRequest request){
        long total = this.customerMapper.queryPhoneCount(request);
        return total;
    }
}
