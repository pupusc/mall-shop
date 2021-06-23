package com.wanmi.sbc.order.trade.service;


import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelMapByCustomerIdAndStoreIdsRequest;
import com.wanmi.sbc.customer.api.response.level.CustomerLevelMapGetResponse;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.order.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 存放订单与会员服务相关的接口方法
 * @author wanggang
 */
@Service
public class TradeCustomerService {

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private RedisService redisService;

    /**
     * 根据店铺ID集合、会员ID查询店铺等级集合
     * @param storeIds
     * @param customerId
     * @return
     */
    public Map<Long, CommonLevelVO> listCustomerLevelMapByCustomerIdAndIds(List<Long> storeIds,String customerId){
        String key = customerId + storeIds.toString();
        String value = redisService.getString(key);
        CustomerLevelMapGetResponse response;
        if (StringUtils.isNotBlank(value)){
           response = JSONObject.parseObject(value,CustomerLevelMapGetResponse.class);
        }else{
            CustomerLevelMapByCustomerIdAndStoreIdsRequest request = new CustomerLevelMapByCustomerIdAndStoreIdsRequest();
            request.setCustomerId(customerId);
            request.setStoreIds(storeIds);
            response = customerLevelQueryProvider.listCustomerLevelMapByCustomerIdAndIds(request).getContext();
            redisService.setString(key,JSONObject.toJSONString(response),60);
        }
        return response.getCommonLevelVOMap();
    }


}
