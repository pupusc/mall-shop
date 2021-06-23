package com.wanmi.sbc.marketing.provider.impl.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.provider.distribution.DistributionCacheQueryProvider;
import com.wanmi.sbc.marketing.api.response.distribution.MultistageSettingGetResponse;
import com.wanmi.sbc.marketing.distribution.service.DistributionCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午11:20 2019/6/19
 * @Description: 分销设置缓存查询服务Controller
 */
@RestController
@Validated
public class DistributionCacheQueryController implements DistributionCacheQueryProvider {

    @Autowired
    private DistributionCacheService distributionCacheService;

    @Override
    public BaseResponse<MultistageSettingGetResponse> getMultistageSetting() {
        return BaseResponse.success(distributionCacheService.getMultistageSetting());
    }

}
