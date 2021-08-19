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
import java.util.Objects;

/**
 * 查询成长值增长通用方法---当次增加XX，限额XXX
 */
@Component
public class DefaultLimitBuilder implements GrowthValueType {

    @Autowired
    private CustomerGrowthValueRepository customerGrowthValueRepository;

    @Override
    public List<GrowthValueServiceType> supportGrowthValueType() {
        List<GrowthValueServiceType> serviceTypeList = new ArrayList<>();
        serviceTypeList.add(GrowthValueServiceType.SHARE);
        serviceTypeList.add(GrowthValueServiceType.EVALUATE);
        serviceTypeList.add(GrowthValueServiceType.SHAREREGISTER);
        serviceTypeList.add(GrowthValueServiceType.SHAREPURCHASE);
        serviceTypeList.add(GrowthValueServiceType.SIGNIN);
        return serviceTypeList;
    }

    @Override
    public Long getIncreaseGrowthValue(JSONObject growthValueRule, CustomerGrowthValue growthValue) {
        Long growthValueIncrease = growthValueRule.getLong("value");

        Long growthValueLimit = growthValueRule.getLong("limit");// 判断该类型是否有成长值限额
        if (growthValueLimit != null) {
            // 查询会员已获取的同类型成长值明细统计
            CustomerGrowthValueQueryRequest queryReq = CustomerGrowthValueQueryRequest.builder()
                    .customerId(growthValue.getCustomerId())
                    .growthValueServiceType(growthValue.getServiceType())
                    .build();
            List<CustomerGrowthValue> growthValueList = customerGrowthValueRepository.findAll(
                    CustomerGrowthValueWhereCriteriaBuilder.build(queryReq));
            Long growthValueCount = growthValueList.stream() // 已获取的该类型成长值
                    .map(CustomerGrowthValue::getGrowthValue)
                    .filter(Objects::nonNull)
                    .reduce(0L, (a, b) -> a + b);
            if (growthValueCount >= growthValueLimit) return 0L;// 成长值超出限额

            Long growthableValue = growthValueLimit - growthValueCount;
            growthValueIncrease = growthableValue > growthValueIncrease ? growthValueIncrease : growthableValue;
        }

        return growthValueIncrease;// 本次增长的成长值
    }
}
