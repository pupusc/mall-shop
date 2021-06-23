package com.wanmi.sbc.customer.growthvalue.builder;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValueQueryRequest;
import com.wanmi.sbc.customer.bean.enums.GrowthValueServiceType;
import com.wanmi.sbc.customer.growthvalue.model.root.CustomerGrowthValue;
import com.wanmi.sbc.customer.growthvalue.repository.CustomerGrowthValueRepository;
import com.wanmi.sbc.customer.growthvalue.service.CustomerGrowthValueWhereCriteriaBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Component
public class FocusonStoreBuilder implements GrowthValueType {

    @Autowired
    private CustomerGrowthValueRepository customerGrowthValueRepository;

    @Override
    public List<GrowthValueServiceType> supportGrowthValueType() {
        return Collections.singletonList(GrowthValueServiceType.FOCUSONSTORE);
    }

    @Override
    public Long getIncreaseGrowthValue(JSONObject growthValueRule, CustomerGrowthValue growthValue) {
        Long growthValueIncrease = growthValueRule.getLong("value");
        // 查询会员已获取的同类型成长值明细统计
        CustomerGrowthValueQueryRequest queryReq = CustomerGrowthValueQueryRequest.builder()
                .customerId(growthValue.getCustomerId())
                .growthValueServiceType(growthValue.getServiceType())
                .build();
        List<CustomerGrowthValue> growthValueList = customerGrowthValueRepository.findAll(
                CustomerGrowthValueWhereCriteriaBuilder.build(queryReq));

        Long storeId = JSONObject.parseObject(growthValue.getContent()).getLong("storeId");
        // 店铺仅第一次关注可获得成长值
        List<CustomerGrowthValue> growthValueByStoreId = growthValueList.stream()
                .filter(g -> {
                    JSONObject content = JSONObject.parseObject(g.getContent());
                    return Objects.nonNull(content) && content.getLong("storeId").equals(storeId);
                }).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(growthValueByStoreId)) return 0L;// 曾关注此店铺获取过成长值

        Long growthValueLimit = growthValueRule.getLong("limit");// 判断该类型是否有成长值限额
        if (growthValueLimit != null) {
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
