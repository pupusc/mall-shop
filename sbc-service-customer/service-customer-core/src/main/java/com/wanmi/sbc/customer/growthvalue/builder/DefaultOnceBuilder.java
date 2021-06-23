package com.wanmi.sbc.customer.growthvalue.builder;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValueQueryRequest;
import com.wanmi.sbc.customer.bean.enums.GrowthValueServiceType;
import com.wanmi.sbc.customer.growthvalue.model.root.CustomerGrowthValue;
import com.wanmi.sbc.customer.growthvalue.repository.CustomerGrowthValueRepository;
import com.wanmi.sbc.customer.growthvalue.service.CustomerGrowthValueWhereCriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询成长值增长通用方法---会员仅可获得一次
 */
@Component
public class DefaultOnceBuilder implements GrowthValueType {

    @Autowired
    private CustomerGrowthValueRepository customerGrowthValueRepository;

    @Override
    public List<GrowthValueServiceType> supportGrowthValueType() {
        List<GrowthValueServiceType> serviceTypeList = new ArrayList<>();
        serviceTypeList.add(GrowthValueServiceType.REGISTER);
        serviceTypeList.add(GrowthValueServiceType.BINDINGWECHAT);
        serviceTypeList.add(GrowthValueServiceType.PERFECTINFO);
        serviceTypeList.add(GrowthValueServiceType.ADDSHIPPINGADDRESS);
        return serviceTypeList;
    }

    @Override
    public Long getIncreaseGrowthValue(JSONObject growthValueRule, CustomerGrowthValue growthValue) {
        // 查询会员已获取的同类型成长值明细统计
        CustomerGrowthValueQueryRequest queryReq = CustomerGrowthValueQueryRequest.builder()
                .customerId(growthValue.getCustomerId())
                .growthValueServiceType(growthValue.getServiceType())
                .build();
        Long growthValueListSize = customerGrowthValueRepository.count(
                CustomerGrowthValueWhereCriteriaBuilder.build(queryReq));
        // 通过该方式获得成长值，每个会员仅可获得一次
        if (growthValueListSize > 0) return 0L;

        return growthValueRule.getLong("value");// 本次增长的成长值
    }
}
