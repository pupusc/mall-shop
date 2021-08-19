package com.wanmi.sbc.setting.provider.impl.umengpushconfig;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.umengpushconfig.UmengPushConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigByIdRequest;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigListRequest;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigPageRequest;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigQueryRequest;
import com.wanmi.sbc.setting.api.response.umengpushconfig.UmengPushConfigByIdResponse;
import com.wanmi.sbc.setting.api.response.umengpushconfig.UmengPushConfigListResponse;
import com.wanmi.sbc.setting.api.response.umengpushconfig.UmengPushConfigPageResponse;
import com.wanmi.sbc.setting.bean.vo.UmengPushConfigVO;
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
 * <p>友盟push接口配置查询服务接口实现</p>
 * @author bob
 * @date 2020-01-07 10:34:04
 */
@RestController
@Validated
public class UmengPushConfigQueryController implements UmengPushConfigQueryProvider {
	@Autowired
	private UmengPushConfigService umengPushConfigService;

	@Override
	public BaseResponse<UmengPushConfigPageResponse> page(@RequestBody @Valid UmengPushConfigPageRequest umengPushConfigPageReq) {
		UmengPushConfigQueryRequest queryReq = new UmengPushConfigQueryRequest();
		KsBeanUtil.copyPropertiesThird(umengPushConfigPageReq, queryReq);
		Page<UmengPushConfig> umengPushConfigPage = umengPushConfigService.page(queryReq);
		Page<UmengPushConfigVO> newPage = umengPushConfigPage.map(entity -> umengPushConfigService.wrapperVo(entity));
		MicroServicePage<UmengPushConfigVO> microPage = new MicroServicePage<>(newPage, umengPushConfigPageReq.getPageable());
		UmengPushConfigPageResponse finalRes = new UmengPushConfigPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<UmengPushConfigListResponse> list(@RequestBody @Valid UmengPushConfigListRequest umengPushConfigListReq) {
		UmengPushConfigQueryRequest queryReq = new UmengPushConfigQueryRequest();
		KsBeanUtil.copyPropertiesThird(umengPushConfigListReq, queryReq);
		List<UmengPushConfig> umengPushConfigList = umengPushConfigService.list(queryReq);
		List<UmengPushConfigVO> newList = umengPushConfigList.stream().map(entity -> umengPushConfigService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new UmengPushConfigListResponse(newList));
	}

	@Override
	public BaseResponse<UmengPushConfigByIdResponse> getById(@RequestBody @Valid UmengPushConfigByIdRequest umengPushConfigByIdRequest) {
		Optional<UmengPushConfig> umengPushConfig =
		umengPushConfigService.getById(umengPushConfigByIdRequest.getId());
		return BaseResponse.success(new UmengPushConfigByIdResponse(umengPushConfigService.wrapperVo(umengPushConfig.orElse(null))));
	}

}

