package com.wanmi.sbc.customer.provider.impl.enterpriseinfo;

import com.wanmi.sbc.customer.api.request.enterpriseinfo.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.provider.enterpriseinfo.EnterpriseInfoProvider;
import com.wanmi.sbc.customer.api.response.enterpriseinfo.EnterpriseInfoAddResponse;
import com.wanmi.sbc.customer.api.response.enterpriseinfo.EnterpriseInfoModifyResponse;
import com.wanmi.sbc.customer.enterpriseinfo.service.EnterpriseInfoService;
import com.wanmi.sbc.customer.enterpriseinfo.model.root.EnterpriseInfo;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

/**
 * <p>企业信息表保存服务接口实现</p>
 * @author TangLian
 * @date 2020-03-02 19:05:06
 */
@RestController
@Validated
public class EnterpriseInfoController implements EnterpriseInfoProvider {
	@Autowired
	private EnterpriseInfoService enterpriseInfoService;

	@Override
	public BaseResponse<EnterpriseInfoAddResponse> add(@RequestBody @Valid EnterpriseInfoAddRequest enterpriseInfoAddRequest) {
		EnterpriseInfo enterpriseInfo = KsBeanUtil.convert(enterpriseInfoAddRequest, EnterpriseInfo.class);
		return BaseResponse.success(new EnterpriseInfoAddResponse(
				enterpriseInfoService.wrapperVo(enterpriseInfoService.add(enterpriseInfo))));
	}

	@Override
	public BaseResponse<EnterpriseInfoModifyResponse> modify(@RequestBody @Valid EnterpriseInfoModifyRequest enterpriseInfoModifyRequest) {
		EnterpriseInfo enterpriseInfo = KsBeanUtil.convert(enterpriseInfoModifyRequest, EnterpriseInfo.class);
		return BaseResponse.success(new EnterpriseInfoModifyResponse(
				enterpriseInfoService.wrapperVo(enterpriseInfoService.modify(enterpriseInfo))));
	}

	@Override
	public BaseResponse<EnterpriseInfoModifyResponse> modifyForWeb(@RequestBody @Valid EnterpriseInfoModifyForWebRequest request) {
		EnterpriseInfo enterpriseInfo = enterpriseInfoService.getByCustomerId(request.getCustomerId());
		KsBeanUtil.copyProperties(request,enterpriseInfo);
		enterpriseInfo.setBusinessEmployeeNum(request.getBusinessEmployeeNum());
		enterpriseInfo.setBusinessIndustryType(request.getBusinessIndustryType());
		return BaseResponse.success(new EnterpriseInfoModifyResponse(
				enterpriseInfoService.wrapperVo(enterpriseInfoService.modify(enterpriseInfo))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid EnterpriseInfoDelByIdRequest enterpriseInfoDelByIdRequest) {
		EnterpriseInfo enterpriseInfo = KsBeanUtil.convert(enterpriseInfoDelByIdRequest, EnterpriseInfo.class);
		enterpriseInfo.setDelFlag(DeleteFlag.YES);
		enterpriseInfoService.deleteById(enterpriseInfo);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid EnterpriseInfoDelByIdListRequest enterpriseInfoDelByIdListRequest) {
		List<EnterpriseInfo> enterpriseInfoList = enterpriseInfoDelByIdListRequest.getEnterpriseIdList().stream()
			.map(EnterpriseId -> {
				EnterpriseInfo enterpriseInfo = KsBeanUtil.convert(EnterpriseId, EnterpriseInfo.class);
				enterpriseInfo.setDelFlag(DeleteFlag.YES);
				return enterpriseInfo;
			}).collect(Collectors.toList());
		enterpriseInfoService.deleteByIdList(enterpriseInfoList);
		return BaseResponse.SUCCESSFUL();
	}

}

