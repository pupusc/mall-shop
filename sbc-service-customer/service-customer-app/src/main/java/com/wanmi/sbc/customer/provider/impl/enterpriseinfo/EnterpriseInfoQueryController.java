package com.wanmi.sbc.customer.provider.impl.enterpriseinfo;

import com.wanmi.sbc.customer.api.request.enterpriseinfo.*;
import com.wanmi.sbc.customer.api.response.enterpriseinfo.EnterpriseInfoByCustomerIdResponse;
import com.wanmi.sbc.customer.api.request.enterpriseinfo.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.enterpriseinfo.EnterpriseInfoQueryProvider;
import com.wanmi.sbc.customer.api.response.enterpriseinfo.EnterpriseInfoPageResponse;
import com.wanmi.sbc.customer.api.response.enterpriseinfo.EnterpriseInfoListResponse;
import com.wanmi.sbc.customer.api.response.enterpriseinfo.EnterpriseInfoByIdResponse;
import com.wanmi.sbc.customer.bean.vo.EnterpriseInfoVO;
import com.wanmi.sbc.customer.enterpriseinfo.service.EnterpriseInfoService;
import com.wanmi.sbc.customer.enterpriseinfo.model.root.EnterpriseInfo;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>企业信息表查询服务接口实现</p>
 * @author TangLian
 * @date 2020-03-02 19:05:06
 */
@RestController
@Validated
public class EnterpriseInfoQueryController implements EnterpriseInfoQueryProvider {
	@Autowired
	private EnterpriseInfoService enterpriseInfoService;

	@Override
	public BaseResponse<EnterpriseInfoPageResponse> page(@RequestBody @Valid EnterpriseInfoPageRequest enterpriseInfoPageReq) {
		EnterpriseInfoQueryRequest queryReq = KsBeanUtil.convert(enterpriseInfoPageReq, EnterpriseInfoQueryRequest.class);
		Page<EnterpriseInfo> enterpriseInfoPage = enterpriseInfoService.page(queryReq);
		Page<EnterpriseInfoVO> newPage = enterpriseInfoPage.map(entity -> enterpriseInfoService.wrapperVo(entity));
		MicroServicePage<EnterpriseInfoVO> microPage = new MicroServicePage<>(newPage, enterpriseInfoPageReq.getPageable());
		EnterpriseInfoPageResponse finalRes = new EnterpriseInfoPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<EnterpriseInfoListResponse> list(@RequestBody @Valid EnterpriseInfoListRequest enterpriseInfoListReq) {
		EnterpriseInfoQueryRequest queryReq = KsBeanUtil.convert(enterpriseInfoListReq, EnterpriseInfoQueryRequest.class);
		List<EnterpriseInfo> enterpriseInfoList = enterpriseInfoService.list(queryReq);
		List<EnterpriseInfoVO> newList = enterpriseInfoList.stream().map(entity -> enterpriseInfoService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new EnterpriseInfoListResponse(newList));
	}

	@Override
	public BaseResponse<EnterpriseInfoByIdResponse> getById(@RequestBody @Valid EnterpriseInfoByIdRequest enterpriseInfoByIdRequest) {
		EnterpriseInfo enterpriseInfo =
		enterpriseInfoService.getOne(enterpriseInfoByIdRequest.getEnterpriseId());
		return BaseResponse.success(new EnterpriseInfoByIdResponse(enterpriseInfoService.wrapperVo(enterpriseInfo)));
	}

	@Override
	public BaseResponse<EnterpriseInfoListResponse> listByCustomerIdList(@RequestBody @Valid EnterpriseInfoListByCustomerIdsRequest request) {
		List<EnterpriseInfo> enterpriseInfoList = enterpriseInfoService.listByCustomerIds(request.getCustomerIdList());
		List<EnterpriseInfoVO> newList = enterpriseInfoList.stream().map(entity -> enterpriseInfoService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new EnterpriseInfoListResponse(newList));
	}

	@Override
	public BaseResponse<EnterpriseInfoByCustomerIdResponse> getByCustomerId(@Valid EnterpriseInfoByCustomerIdRequest enterpriseInfoByCustomerIdRequest) {
		EnterpriseInfo enterpriseInfo =enterpriseInfoService.getByCustomerId(enterpriseInfoByCustomerIdRequest.getCustomerId());
		return BaseResponse.success(new EnterpriseInfoByCustomerIdResponse(enterpriseInfoService.wrapperVo(enterpriseInfo)));
	}
}

