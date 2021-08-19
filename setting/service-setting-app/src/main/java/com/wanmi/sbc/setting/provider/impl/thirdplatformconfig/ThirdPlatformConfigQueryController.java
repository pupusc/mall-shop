package com.wanmi.sbc.setting.provider.impl.thirdplatformconfig;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.thirdplatformconfig.ThirdPlatformConfigQueryProvider;
import com.wanmi.sbc.setting.api.provider.umengpushconfig.UmengPushConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.thirdplatformconfig.ThirdPlatformConfigByTypeRequest;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigByIdRequest;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigListRequest;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigPageRequest;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigQueryRequest;
import com.wanmi.sbc.setting.api.response.thirdaddress.ThirdAddressPageResponse;
import com.wanmi.sbc.setting.api.response.thirdplatformconfig.ThirdPlatformConfigQueryResponse;
import com.wanmi.sbc.setting.api.response.thirdplatformconfig.ThirdPlatformConfigResponse;
import com.wanmi.sbc.setting.api.response.umengpushconfig.UmengPushConfigByIdResponse;
import com.wanmi.sbc.setting.api.response.umengpushconfig.UmengPushConfigListResponse;
import com.wanmi.sbc.setting.api.response.umengpushconfig.UmengPushConfigPageResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import com.wanmi.sbc.setting.bean.vo.UmengPushConfigVO;
import com.wanmi.sbc.setting.config.Config;
import com.wanmi.sbc.setting.config.ConfigService;
import com.wanmi.sbc.setting.thirdplatformconfig.service.ThirdPlatformConfigService;
import com.wanmi.sbc.setting.umengpushconfig.model.root.UmengPushConfig;
import com.wanmi.sbc.setting.umengpushconfig.service.UmengPushConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>第三方平台配置查询服务接口实现</p>
 * @author bob
 * @date 2020-01-07 10:34:04
 */
@RestController
@Validated
public class ThirdPlatformConfigQueryController implements ThirdPlatformConfigQueryProvider {
	@Autowired
	private ThirdPlatformConfigService thirdPlatformConfigService;

	@Autowired
	private ConfigService configService;

	@Override
	public BaseResponse<ThirdPlatformConfigQueryResponse> list() {
		List<Config> configs = configService.findByConfigKeyAndDelFlag(ConfigKey.THIRD_PLATFORM_SETTING.toValue(), DeleteFlag.NO);
		return BaseResponse.success(ThirdPlatformConfigQueryResponse.builder().configVOList(KsBeanUtil.convert(configs, ConfigVO.class)).build());
	}

	public BaseResponse<ThirdPlatformConfigResponse> get(@RequestBody @Valid ThirdPlatformConfigByTypeRequest request) {
		//linked-Mall配置
		if(ConfigType.THIRD_PLATFORM_LINKED_MALL.toValue().equals(request.getConfigType())){
			return BaseResponse.success(thirdPlatformConfigService.findByLinkedMall());
		}
		return BaseResponse.SUCCESSFUL();
	}
}

