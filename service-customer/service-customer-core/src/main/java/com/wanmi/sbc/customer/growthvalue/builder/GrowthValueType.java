package com.wanmi.sbc.customer.growthvalue.builder;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.customer.bean.enums.GrowthValueServiceType;
import com.wanmi.sbc.customer.growthvalue.model.root.CustomerGrowthValue;

import java.util.List;

public interface GrowthValueType {

    List<GrowthValueServiceType> supportGrowthValueType();

    Long getIncreaseGrowthValue(JSONObject growthValueRule, CustomerGrowthValue growthValue);

}
