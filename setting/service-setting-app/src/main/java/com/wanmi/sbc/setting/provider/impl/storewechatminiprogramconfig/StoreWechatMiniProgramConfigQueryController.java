package com.wanmi.sbc.setting.provider.impl.storewechatminiprogramconfig;

import com.alibaba.fastjson.JSONObject;
//import com.google.gson.JsonObject;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.redis.util.RedisStoreUtil;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.setting.api.request.storewechatminiprogramconfig.*;
import com.wanmi.sbc.setting.api.response.storewechatminiprogramconfig.*;
import com.wanmi.sbc.setting.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.storewechatminiprogramconfig.StoreWechatMiniProgramConfigQueryProvider;
import com.wanmi.sbc.setting.bean.vo.StoreWechatMiniProgramConfigVO;
import com.wanmi.sbc.setting.storewechatminiprogramconfig.service.StoreWechatMiniProgramConfigService;
import com.wanmi.sbc.setting.storewechatminiprogramconfig.model.root.StoreWechatMiniProgramConfig;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>门店微信小程序配置查询服务接口实现</p>
 * @author tangLian
 * @date 2020-01-16 11:47:15
 */
@RestController
@Validated
public class StoreWechatMiniProgramConfigQueryController implements StoreWechatMiniProgramConfigQueryProvider {
	@Autowired
	private StoreWechatMiniProgramConfigService storeWechatMiniProgramConfigService;

	@Autowired
	private RedisService redisService;

	@Override
	public BaseResponse<StoreWechatMiniProgramConfigPageResponse> page(@RequestBody @Valid StoreWechatMiniProgramConfigPageRequest storeWechatMiniProgramConfigPageReq) {
		StoreWechatMiniProgramConfigQueryRequest queryReq = new StoreWechatMiniProgramConfigQueryRequest();
		KsBeanUtil.copyPropertiesThird(storeWechatMiniProgramConfigPageReq, queryReq);
		Page<StoreWechatMiniProgramConfig> storeWechatMiniProgramConfigPage = storeWechatMiniProgramConfigService.page(queryReq);
		Page<StoreWechatMiniProgramConfigVO> newPage = storeWechatMiniProgramConfigPage.map(entity -> storeWechatMiniProgramConfigService.wrapperVo(entity));
		MicroServicePage<StoreWechatMiniProgramConfigVO> microPage = new MicroServicePage<>(newPage, storeWechatMiniProgramConfigPageReq.getPageable());
		StoreWechatMiniProgramConfigPageResponse finalRes = new StoreWechatMiniProgramConfigPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<StoreWechatMiniProgramConfigListResponse> list(@RequestBody @Valid StoreWechatMiniProgramConfigListRequest storeWechatMiniProgramConfigListReq) {
		StoreWechatMiniProgramConfigQueryRequest queryReq = new StoreWechatMiniProgramConfigQueryRequest();
		KsBeanUtil.copyPropertiesThird(storeWechatMiniProgramConfigListReq, queryReq);
		List<StoreWechatMiniProgramConfig> storeWechatMiniProgramConfigList = storeWechatMiniProgramConfigService.list(queryReq);
		List<StoreWechatMiniProgramConfigVO> newList = storeWechatMiniProgramConfigList.stream().map(entity -> storeWechatMiniProgramConfigService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new StoreWechatMiniProgramConfigListResponse(newList));
	}

	@Override
	public BaseResponse<StoreWechatMiniProgramConfigByIdResponse> getById(@RequestBody @Valid StoreWechatMiniProgramConfigByIdRequest storeWechatMiniProgramConfigByIdRequest) {
		StoreWechatMiniProgramConfig storeWechatMiniProgramConfig = storeWechatMiniProgramConfigService.getById(storeWechatMiniProgramConfigByIdRequest.getId());
		return BaseResponse.success(new StoreWechatMiniProgramConfigByIdResponse(storeWechatMiniProgramConfigService.wrapperVo(storeWechatMiniProgramConfig)));
	}

	@Override
	public BaseResponse<StoreWechatMiniProgramConfigByStoreIdResponse> getByStoreId(@RequestBody @Valid StoreWechatMiniProgramConfigByStoreIdRequest request) {
		List<StoreWechatMiniProgramConfig> list = storeWechatMiniProgramConfigService.list(StoreWechatMiniProgramConfigQueryRequest.builder()
				.storeId(request.getStoreId())
				.delFlag(DeleteFlag.NO)
				.build());
		if(list ==null
				|| list.size() ==0){
			return BaseResponse.success(null);
		}
		return BaseResponse.success(StoreWechatMiniProgramConfigByStoreIdResponse.builder()
				.storeWechatMiniProgramConfigVO(storeWechatMiniProgramConfigService.wrapperVo(list.get(0)))
				.build());
	}

	@Override
	public BaseResponse<StoreWechatMiniProgramConfigByCacheResponse> getCacheByStoreId(@RequestBody @Valid StoreWechatMiniProgramConfigByCacheRequest request) {
		String config = redisService.getString(RedisStoreUtil.getWechatMiniProgramConfig(request.getStoreId()));
		if(StringUtils.isNoneEmpty(config)){
			return BaseResponse.success(StoreWechatMiniProgramConfigByCacheResponse.builder()
					.storeWechatMiniProgramConfigVO(JSONObject.parseObject(config,StoreWechatMiniProgramConfigVO.class))
					.build());
		}
		BaseResponse<StoreWechatMiniProgramConfigByStoreIdResponse> byStoreId = getByStoreId(StoreWechatMiniProgramConfigByStoreIdRequest.builder()
				.storeId(request.getStoreId())
				.build());
		return BaseResponse.success(StoreWechatMiniProgramConfigByCacheResponse.builder()
				.storeWechatMiniProgramConfigVO(byStoreId.getContext().getStoreWechatMiniProgramConfigVO())
				.build());
	}
}

