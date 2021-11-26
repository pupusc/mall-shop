package com.wanmi.sbc.crm.provider.impl.autotagother;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.crm.api.provider.autotagOther.AutoTagOtherQueryProvider;
import com.wanmi.sbc.crm.api.request.autotagother.AutoTagOtherPageRequest;
import com.wanmi.sbc.crm.api.response.autotagother.AutotagOtherPageResponse;
import com.wanmi.sbc.crm.autotagother.model.AutotagOther;
import com.wanmi.sbc.crm.autotagother.service.AutotagOtherService;
import com.wanmi.sbc.crm.bean.vo.AutotagOtherVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
public class AutotagOtherQueryController implements AutoTagOtherQueryProvider {

	@Autowired
	private AutotagOtherService autotagOtherService;

	@Override
	public BaseResponse<AutotagOtherPageResponse> page(@Valid AutoTagOtherPageRequest autoTagOtherPageRequest) {
		List<AutotagOther> autotagOthers = autotagOtherService.pageByTagId(autoTagOtherPageRequest);
		List<AutotagOtherVO> autotagOtherVOS =
				autotagOthers.stream().map(entity -> autotagOtherService.wrapperVo(entity)).collect(Collectors.toList());
		long count = autotagOtherService.countByTagIdAndDetailName(autoTagOtherPageRequest);

		MicroServicePage<AutotagOtherVO> microPage = new MicroServicePage<>(autotagOtherVOS,
				PageRequest.of(autoTagOtherPageRequest.getPageNum(), autoTagOtherPageRequest.getPageSize()), count);
		return BaseResponse.success(AutotagOtherPageResponse.builder().autotagOtherVOS(microPage).build());
	}
}

