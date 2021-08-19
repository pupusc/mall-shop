package com.wanmi.sbc.setting.provider.impl.businessconfig;

import com.wanmi.sbc.setting.api.response.businessconfig.BusinessConfigRopResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.businessconfig.BusinessConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.businessconfig.BusinessConfigPageRequest;
import com.wanmi.sbc.setting.api.request.businessconfig.BusinessConfigQueryRequest;
import com.wanmi.sbc.setting.api.response.businessconfig.BusinessConfigPageResponse;
import com.wanmi.sbc.setting.api.request.businessconfig.BusinessConfigListRequest;
import com.wanmi.sbc.setting.api.response.businessconfig.BusinessConfigListResponse;
import com.wanmi.sbc.setting.api.request.businessconfig.BusinessConfigByIdRequest;
import com.wanmi.sbc.setting.api.response.businessconfig.BusinessConfigByIdResponse;
import com.wanmi.sbc.setting.bean.vo.BusinessConfigVO;
import com.wanmi.sbc.setting.businessconfig.service.BusinessConfigService;
import com.wanmi.sbc.setting.businessconfig.model.root.BusinessConfig;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>招商页设置查询服务接口实现</p>
 * @author lq
 * @date 2019-11-05 16:09:10
 */
@RestController
@Validated
public class BusinessConfigQueryController implements BusinessConfigQueryProvider {
	@Autowired
	private BusinessConfigService businessConfigService;

	@Override
	public BaseResponse<BusinessConfigPageResponse> page(@RequestBody @Valid BusinessConfigPageRequest businessConfigPageReq) {
		BusinessConfigQueryRequest queryReq = new BusinessConfigQueryRequest();
		KsBeanUtil.copyPropertiesThird(businessConfigPageReq, queryReq);
		Page<BusinessConfig> businessConfigPage = businessConfigService.page(queryReq);
		Page<BusinessConfigVO> newPage = businessConfigPage.map(entity -> businessConfigService.wrapperVo(entity));
		MicroServicePage<BusinessConfigVO> microPage = new MicroServicePage<>(newPage, businessConfigPageReq.getPageable());
		BusinessConfigPageResponse finalRes = new BusinessConfigPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<BusinessConfigListResponse> list(@RequestBody @Valid BusinessConfigListRequest businessConfigListReq) {
		BusinessConfigQueryRequest queryReq = new BusinessConfigQueryRequest();
		KsBeanUtil.copyPropertiesThird(businessConfigListReq, queryReq);
		List<BusinessConfig> businessConfigList = businessConfigService.list(queryReq);
		List<BusinessConfigVO> newList = businessConfigList.stream().map(entity -> businessConfigService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new BusinessConfigListResponse(newList));
	}

	@Override
	public BaseResponse<BusinessConfigByIdResponse> getById(@RequestBody @Valid BusinessConfigByIdRequest businessConfigByIdRequest) {
		BusinessConfig businessConfig = businessConfigService.getById(businessConfigByIdRequest.getBusinessConfigId());
		return BaseResponse.success(new BusinessConfigByIdResponse(businessConfigService.wrapperVo(businessConfig)));
	}

	@Override
	public BaseResponse<BusinessConfigRopResponse> getInfo() {
		BusinessConfig businessConfig = businessConfigService.getInfo();
		return BaseResponse.success(KsBeanUtil.convert(businessConfig,BusinessConfigRopResponse.class));
	}

}

