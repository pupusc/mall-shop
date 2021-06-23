package com.wanmi.sbc.setting.provider.impl.storewechatminiprogramconfig;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.redis.util.RedisStoreUtil;
import com.wanmi.sbc.setting.redis.RedisService;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.support.collections.RedisStore;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.storewechatminiprogramconfig.StoreWechatMiniProgramConfigSaveProvider;
import com.wanmi.sbc.setting.api.request.storewechatminiprogramconfig.StoreWechatMiniProgramConfigAddRequest;
import com.wanmi.sbc.setting.api.response.storewechatminiprogramconfig.StoreWechatMiniProgramConfigAddResponse;
import com.wanmi.sbc.setting.api.request.storewechatminiprogramconfig.StoreWechatMiniProgramConfigModifyRequest;
import com.wanmi.sbc.setting.api.response.storewechatminiprogramconfig.StoreWechatMiniProgramConfigModifyResponse;
import com.wanmi.sbc.setting.api.request.storewechatminiprogramconfig.StoreWechatMiniProgramConfigDelByIdRequest;
import com.wanmi.sbc.setting.api.request.storewechatminiprogramconfig.StoreWechatMiniProgramConfigDelByIdListRequest;
import com.wanmi.sbc.setting.storewechatminiprogramconfig.service.StoreWechatMiniProgramConfigService;
import com.wanmi.sbc.setting.storewechatminiprogramconfig.model.root.StoreWechatMiniProgramConfig;
import javax.validation.Valid;

/**
 * <p>门店微信小程序配置保存服务接口实现</p>
 * @author tangLian
 * @date 2020-01-16 11:47:15
 */
@RestController
@Validated
public class StoreWechatMiniProgramConfigSaveController implements StoreWechatMiniProgramConfigSaveProvider {
	@Autowired
	private StoreWechatMiniProgramConfigService storeWechatMiniProgramConfigService;
	@Autowired
	private RedisService redisService;

	@Override
	public BaseResponse<StoreWechatMiniProgramConfigAddResponse> add(@RequestBody @Valid StoreWechatMiniProgramConfigAddRequest storeWechatMiniProgramConfigAddRequest) {
		StoreWechatMiniProgramConfig storeWechatMiniProgramConfig = new StoreWechatMiniProgramConfig();
		KsBeanUtil.copyPropertiesThird(storeWechatMiniProgramConfigAddRequest, storeWechatMiniProgramConfig);
		StoreWechatMiniProgramConfig newConfig = storeWechatMiniProgramConfigService.add(storeWechatMiniProgramConfig);
		updateCache(newConfig);
		return BaseResponse.success(new StoreWechatMiniProgramConfigAddResponse(
				storeWechatMiniProgramConfigService.wrapperVo(newConfig)));
	}

	@Override
	public BaseResponse<StoreWechatMiniProgramConfigModifyResponse> modify(@RequestBody @Valid StoreWechatMiniProgramConfigModifyRequest storeWechatMiniProgramConfigModifyRequest) {
		StoreWechatMiniProgramConfig storeWechatMiniProgramConfig = new StoreWechatMiniProgramConfig();
		KsBeanUtil.copyPropertiesThird(storeWechatMiniProgramConfigModifyRequest, storeWechatMiniProgramConfig);
		StoreWechatMiniProgramConfig newConfig = storeWechatMiniProgramConfigService.modify(storeWechatMiniProgramConfig);
		updateCache(newConfig);
		return BaseResponse.success(new StoreWechatMiniProgramConfigModifyResponse(
				storeWechatMiniProgramConfigService.wrapperVo(newConfig)));
	}

	/**
	 * 更新Redis缓存
	 */
	private void updateCache(StoreWechatMiniProgramConfig config){
		redisService.setString(RedisStoreUtil.getWechatMiniProgramConfig(config.getStoreId()), JSONObject.toJSONString(config));
	}

}

