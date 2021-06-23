package com.wanmi.sbc.setting.provider.impl.thirdplatformconfig;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.thirdplatformconfig.ThirdPlatformConfigProvider;
import com.wanmi.sbc.setting.api.provider.thirdplatformconfig.ThirdPlatformConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.thirdplatformconfig.ThirdPlatformConfigByTypeRequest;
import com.wanmi.sbc.setting.api.request.thirdplatformconfig.ThirdPlatformConfigModifyRequest;
import com.wanmi.sbc.setting.api.response.thirdplatformconfig.ThirdPlatformConfigQueryResponse;
import com.wanmi.sbc.setting.api.response.thirdplatformconfig.ThirdPlatformConfigResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import com.wanmi.sbc.setting.config.Config;
import com.wanmi.sbc.setting.config.ConfigService;
import com.wanmi.sbc.setting.thirdplatformconfig.service.ThirdPlatformConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>第三方平台配置查询服务接口实现</p>
 * @author bob
 * @date 2020-01-07 10:34:04
 */
@RestController
@Validated
public class ThirdPlatformConfigController implements ThirdPlatformConfigProvider {
	@Autowired
	private ThirdPlatformConfigService thirdPlatformConfigService;

	@Override
	public BaseResponse modify(@RequestBody @Valid ThirdPlatformConfigModifyRequest request) {
		//linked-Mall配置
		if(ConfigType.THIRD_PLATFORM_LINKED_MALL.toValue().equals(request.getConfigType())){
			thirdPlatformConfigService.modifyByLinkedMall(request);
		}
		return BaseResponse.SUCCESSFUL();
	}
}

