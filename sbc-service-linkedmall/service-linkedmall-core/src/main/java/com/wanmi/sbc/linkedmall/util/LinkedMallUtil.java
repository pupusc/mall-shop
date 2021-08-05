package com.wanmi.sbc.linkedmall.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.wanmi.sbc.setting.api.provider.thirdplatformconfig.ThirdPlatformConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.thirdplatformconfig.ThirdPlatformConfigByTypeRequest;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LinkedMallUtil {

    @Value("${REGION_ID}")
    private String regionId;

    @Value("${ACCESS_KEY_ID}")
    private String accessKeyId;

    @Value("${ACCESS_KEY_SECRET}")
    private String accessKeySecret;

    @Autowired
    private ThirdPlatformConfigQueryProvider thirdPlatformConfigQueryProvider;

    public static final String SUCCESS_CODE = "SUCCESS";

    public static final String SUCCESS_CODE_2 = "0000";

    public static final String ACCOUNT_TYPE = "ANONY";

    @Bean
    public IAcsClient getIAcsClient() {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        return new DefaultAcsClient(profile);
    }

    @Cacheable(value = "BIZ_ID")
    public String getLinkedMallBizId(){
        return thirdPlatformConfigQueryProvider.get(ThirdPlatformConfigByTypeRequest.builder().configType(ConfigType.THIRD_PLATFORM_LINKED_MALL.toValue()).build()).getContext().getCustomerBizId();
    }
}
