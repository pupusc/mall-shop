package com.wanmi.ares.source.service;

import com.wanmi.ares.report.customer.dao.CustomerAndLevelMapper;
import com.wanmi.ares.request.mq.CustomerAndLevelRequest;
import com.wanmi.ares.source.model.root.CustomerAndLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * 客户等级
 */
@Slf4j
@Service
public class CustomerAndLevelService {

    @Resource
    private CustomerAndLevelMapper customerAndLevelMapper;

    /**
     * 更新等级ID
     * @param oldLevelId
     * @param newLevelId
     * @return
     */
    public int updateLevelId(String oldLevelId, String newLevelId){
        return customerAndLevelMapper.updateCustomerLevel(oldLevelId, newLevelId);
    }

    /**
     * 更新等级与业务员
     * @param request
     * @return
     */
    public int update(CustomerAndLevelRequest request, LocalDate bindDate){
        // 1.更新customer表的业务员标识
        customerAndLevelMapper.updateEmployeeId(request.getCustomerId(), request.getEmployeeId(), bindDate);
        // 2.更新会员等级关联表中的等级标识与业务员标识
        return customerAndLevelMapper.update(request.getId(),request.getCustomerLevelId(),request.getEmployeeId());
    }

    /**
     * 添加店铺客户等级关系
     * @param customerAndLevel
     * @return
     */
    public int insert(CustomerAndLevel customerAndLevel){
        return customerAndLevelMapper.insert(customerAndLevel);
    }

    /**
     * 删除店铺客户等级关系
     * @param id
     * @return
     */
    public int delete(String id){
        return customerAndLevelMapper.delete(id);
    }

    /**
     * 根据用户id,商家id查询用户等级
     * @param customerId
     * @param companyInfoId
     * @return
     */
    public CustomerAndLevel selectCustomerStoreLevel(String customerId, String companyInfoId){
        return customerAndLevelMapper.selectCustomerStoreLevel(customerId,companyInfoId);
    }
}
