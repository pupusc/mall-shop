package com.wanmi.sbc.customer.provider.impl.livecompany;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.provider.livecompany.LiveCompanyProvider;
import com.wanmi.sbc.customer.api.request.livecompany.LiveCompanyAddRequest;
import com.wanmi.sbc.customer.api.response.livecompany.LiveCompanyAddResponse;
import com.wanmi.sbc.customer.api.request.livecompany.LiveCompanyModifyRequest;
import com.wanmi.sbc.customer.api.response.livecompany.LiveCompanyModifyResponse;
import com.wanmi.sbc.customer.api.request.livecompany.LiveCompanyDelByIdRequest;
import com.wanmi.sbc.customer.api.request.livecompany.LiveCompanyDelByIdListRequest;
import com.wanmi.sbc.customer.livecompany.service.LiveCompanyService;
import com.wanmi.sbc.customer.livecompany.model.root.LiveCompany;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

/**
 * <p>直播商家保存服务接口实现</p>
 * @author zwb
 * @date 2020-06-06 18:06:59
 */
@RestController
@Validated
public class LiveCompanyController implements LiveCompanyProvider {
	@Autowired
	private LiveCompanyService liveCompanyService;

	@Override
	public BaseResponse<LiveCompanyAddResponse> add(@RequestBody @Valid LiveCompanyAddRequest liveCompanyAddRequest) {
		LiveCompany liveCompany = KsBeanUtil.convert(liveCompanyAddRequest, LiveCompany.class);
		return BaseResponse.success(new LiveCompanyAddResponse(
				liveCompanyService.wrapperVo(liveCompanyService.add(liveCompany))));
	}

	@Override
	public BaseResponse<LiveCompanyModifyResponse> modify(@RequestBody @Valid LiveCompanyModifyRequest liveCompanyModifyRequest) {
		LiveCompany liveCompany = KsBeanUtil.convert(liveCompanyModifyRequest, LiveCompany.class);
		return BaseResponse.success(new LiveCompanyModifyResponse(
				liveCompanyService.wrapperVo(liveCompanyService.modify(liveCompany))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid LiveCompanyDelByIdRequest liveCompanyDelByIdRequest) {
		LiveCompany liveCompany = KsBeanUtil.convert(liveCompanyDelByIdRequest, LiveCompany.class);
		liveCompany.setDelFlag(DeleteFlag.YES);
		liveCompanyService.deleteById(liveCompany);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid LiveCompanyDelByIdListRequest liveCompanyDelByIdListRequest) {
		List<LiveCompany> liveCompanyList = liveCompanyDelByIdListRequest.getIdList().stream()
			.map(Id -> {
				LiveCompany liveCompany = KsBeanUtil.convert(Id, LiveCompany.class);
				liveCompany.setDelFlag(DeleteFlag.YES);
				return liveCompany;
			}).collect(Collectors.toList());
		liveCompanyService.deleteByIdList(liveCompanyList);
		return BaseResponse.SUCCESSFUL();
	}

}

