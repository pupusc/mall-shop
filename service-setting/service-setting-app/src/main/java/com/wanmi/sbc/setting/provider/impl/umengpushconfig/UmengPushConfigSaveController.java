package com.wanmi.sbc.setting.provider.impl.umengpushconfig;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.umengpushconfig.UmengPushConfigSaveProvider;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigAddRequest;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigDelByIdListRequest;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigDelByIdRequest;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigModifyRequest;
import com.wanmi.sbc.setting.api.response.umengpushconfig.UmengPushConfigAddResponse;
import com.wanmi.sbc.setting.api.response.umengpushconfig.UmengPushConfigModifyResponse;
import com.wanmi.sbc.setting.bean.vo.UmengPushConfigVO;
import com.wanmi.sbc.setting.redis.RedisService;
import com.wanmi.sbc.setting.umengpushconfig.model.root.UmengPushConfig;
import com.wanmi.sbc.setting.umengpushconfig.service.UmengPushConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>友盟push接口配置保存服务接口实现</p>
 * @author bob
 * @date 2020-01-07 10:34:04
 */
@RestController
@Validated
public class UmengPushConfigSaveController implements UmengPushConfigSaveProvider {
	@Autowired
	private UmengPushConfigService umengPushConfigService;
	@Autowired
	private RedisService redisService;

	@Override
	public BaseResponse<UmengPushConfigAddResponse> add(@RequestBody @Valid UmengPushConfigAddRequest umengPushConfigAddRequest) {
		UmengPushConfig umengPushConfig = new UmengPushConfig();
		KsBeanUtil.copyPropertiesThird(umengPushConfigAddRequest, umengPushConfig);
		UmengPushConfigVO vo = umengPushConfigService.wrapperVo(umengPushConfigService.add(umengPushConfig));
		redisService.put(CacheKeyConstant.UMENG_CONFIG, vo);
		return BaseResponse.success(new UmengPushConfigAddResponse(vo));
	}

	@Override
	public BaseResponse<UmengPushConfigModifyResponse> modify(@RequestBody @Valid UmengPushConfigModifyRequest umengPushConfigModifyRequest) {
		UmengPushConfig umengPushConfig = new UmengPushConfig();
		KsBeanUtil.copyPropertiesThird(umengPushConfigModifyRequest, umengPushConfig);
		return BaseResponse.success(new UmengPushConfigModifyResponse(
				umengPushConfigService.wrapperVo(umengPushConfigService.modify(umengPushConfig))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid UmengPushConfigDelByIdRequest umengPushConfigDelByIdRequest) {
		umengPushConfigService.deleteById(umengPushConfigDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid UmengPushConfigDelByIdListRequest umengPushConfigDelByIdListRequest) {
		umengPushConfigService.deleteByIdList(umengPushConfigDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

