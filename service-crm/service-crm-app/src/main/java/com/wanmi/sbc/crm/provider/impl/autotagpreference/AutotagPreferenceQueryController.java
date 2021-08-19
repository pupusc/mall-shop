package com.wanmi.sbc.crm.provider.impl.autotagpreference;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.crm.api.provider.autotagpreference.AutotagPreferenceQueryProvider;
import com.wanmi.sbc.crm.api.request.autotagpreference.AutoPreferencePageRequest;
import com.wanmi.sbc.crm.api.response.autotagpreference.AutotagPreferencePageResponse;
import com.wanmi.sbc.crm.autotagpreference.model.AutotagPreference;
import com.wanmi.sbc.crm.autotagpreference.service.AutotagPreferenceService;
import com.wanmi.sbc.crm.bean.vo.AutotagPreferenceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>偏好标签明细查询服务接口实现</p>
 * @author dyt
 * @date 2020-03-11 14:58:07
 */
@RestController
@Validated
public class AutotagPreferenceQueryController implements AutotagPreferenceQueryProvider {

	@Autowired
	private AutotagPreferenceService autotagPreferenceService;

	@Override
	public BaseResponse<AutotagPreferencePageResponse> page(@Valid AutoPreferencePageRequest request) {
		List<AutotagPreference> autotagPreferences = autotagPreferenceService.pageByTagId(request);
		List<AutotagPreferenceVO> autotagPreferenceVOS =
				autotagPreferences.stream().map(entity -> autotagPreferenceService.wrapperVo(entity)).collect(Collectors.toList());
		long countByTagIdAndDetailName = autotagPreferenceService.countByTagIdAndDetailName(request);
		long countByTagId = autotagPreferenceService.countByTagId(request.getTagId());
		MicroServicePage<AutotagPreferenceVO> microPage = new MicroServicePage<>(autotagPreferenceVOS,
				PageRequest.of(request.getPageNum(), request.getPageSize()), countByTagIdAndDetailName);
		return BaseResponse.success(AutotagPreferencePageResponse.builder().autotagPreferenceVOPage(microPage)
				.count(countByTagId).build());
	}
}

