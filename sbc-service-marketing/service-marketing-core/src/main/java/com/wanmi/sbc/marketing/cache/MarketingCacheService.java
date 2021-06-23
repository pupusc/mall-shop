package com.wanmi.sbc.marketing.cache;

import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerByCustomerIdRequest;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import com.wanmi.sbc.marketing.api.response.distribution.DistributionSettingGetResponse;
import com.wanmi.sbc.marketing.api.response.distribution.DistributionStoreSettingGetByStoreIdResponse;
import com.wanmi.sbc.marketing.bean.vo.DistributionSettingCacheVO;
import com.wanmi.sbc.marketing.distribution.service.DistributionSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
public class MarketingCacheService {


    @Autowired
    private DistributionCustomerQueryProvider distributionCustomerQueryProvider;

    @Autowired
    private DistributionSettingService distributionSettingService;


    @Cacheable(value = WmCacheConfig.MARKETING, keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public DistributionCustomerVO getDistributorByCustomerId(String customerId) {
        return distributionCustomerQueryProvider.getByCustomerId(
                new DistributionCustomerByCustomerIdRequest(customerId)).getContext().getDistributionCustomerVO();
    }

    /**
     * 查询店铺分销设置
     * @param storeId
     * @return
     */
    @Cacheable(value = WmCacheConfig.MARKETING, keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public DistributionStoreSettingGetByStoreIdResponse queryDistributionStoreSetting(String storeId) {
        return distributionSettingService.queryStoreSetting(storeId);
    }

    /**
     * 查询分销设置
     */
    @Cacheable(value = WmCacheConfig.MARKETING, keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public DistributionSettingCacheVO queryDistributionSetting() {
        DistributionSettingGetResponse response = distributionSettingService.querySetting();
        return DistributionSettingCacheVO.builder()
                .distributionSetting(response.getDistributionSetting())
                .distributorLevels(response.getDistributorLevels())
                .build();
    }
}
