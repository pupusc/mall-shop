package com.wanmi.sbc.setting.thirdplatformconfig.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.setting.api.request.thirdplatformconfig.ThirdPlatformConfigModifyRequest;
import com.wanmi.sbc.setting.api.response.thirdplatformconfig.ThirdPlatformConfigResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.config.Config;
import com.wanmi.sbc.setting.config.ConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>第三方地址映射表业务逻辑</p>
 * @author dyt
 * @date 2020-08-14 13:41:44
 */
@Slf4j
@Service("ThirdPlatformConfigService")
public class ThirdPlatformConfigService {

	@Autowired
	private ConfigRepository configRepository;

	/**
	 * 更新linkedMall
	 * @param request
	 */
	@CacheEvict(value = "THIRD_PLATFORM_CONFIG_LINKEDMALL",allEntries = true)
	@Transactional(rollbackFor = Exception.class)
	public void modifyByLinkedMall(ThirdPlatformConfigModifyRequest request) {
		//查询存不存在更新
		Config oldConfig = configRepository.findByConfigTypeAndConfigKeyAndDelFlag(
				ConfigType.THIRD_PLATFORM_LINKED_MALL.toValue(), ConfigKey.THIRD_PLATFORM_SETTING.toValue(),DeleteFlag.NO);
		if (oldConfig == null) {
			throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("customerBizId", request.getCustomerBizId());
		//将转化后的结果作为context信息拷贝到oldConfig上, 其他的不变
		oldConfig.setContext(jsonObject.toJSONString());
		oldConfig.setUpdateTime(LocalDateTime.now());
		oldConfig.setStatus(request.getStatus());
		configRepository.save(oldConfig);
	}

	/**
	 * 根据linkedMall查询
	 */
	@Cacheable(value = "THIRD_PLATFORM_CONFIG_LINKEDMALL")
	public ThirdPlatformConfigResponse findByLinkedMall() {
		//查询存不存在更新
		Config oldConfig = configRepository.findByConfigTypeAndConfigKeyAndDelFlag(
				ConfigType.THIRD_PLATFORM_LINKED_MALL.toValue(), ConfigKey.THIRD_PLATFORM_SETTING.toValue(), DeleteFlag.NO);
		if(oldConfig == null){
			return null;
		}
		ThirdPlatformConfigResponse response = new ThirdPlatformConfigResponse();
		if(StringUtils.isNotBlank(oldConfig.getContext())){
			response.setCustomerBizId(JSON.parseObject(oldConfig.getContext()).getString("customerBizId"));
		}
		response.setThirdPlatformType(ThirdPlatformType.LINKED_MALL);
		response.setStatus(oldConfig.getStatus());
		return response;
	}
}

