package com.wanmi.sbc.setting.provider.impl.companyinfo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.companyinfo.CompanyInfoQueryProvider;
import com.wanmi.sbc.setting.api.request.companyinfo.CompanyInfoPageRequest;
import com.wanmi.sbc.setting.api.request.companyinfo.CompanyInfoQueryRequest;
import com.wanmi.sbc.setting.api.response.companyinfo.CompanyInfoPageResponse;
import com.wanmi.sbc.setting.api.request.companyinfo.CompanyInfoListRequest;
import com.wanmi.sbc.setting.api.response.companyinfo.CompanyInfoListResponse;
import com.wanmi.sbc.setting.api.request.companyinfo.CompanyInfoByIdRequest;
import com.wanmi.sbc.setting.api.response.companyinfo.CompanyInfoByIdResponse;
import com.wanmi.sbc.setting.api.response.companyinfo.CompanyInfoRopResponse;
import com.wanmi.sbc.setting.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.setting.companyinfo.service.CompanyInfoService;
import com.wanmi.sbc.setting.companyinfo.model.root.CompanyInfo;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>公司信息查询服务接口实现</p>
 * @author lq
 * @date 2019-11-05 16:09:36
 */
@RestController
@Validated
public class CompanyInfoQueryController implements CompanyInfoQueryProvider {
	@Autowired
	private CompanyInfoService companyInfoService;

	@Override
	public BaseResponse<CompanyInfoPageResponse> page(@RequestBody @Valid CompanyInfoPageRequest companyInfoPageReq) {
		CompanyInfoQueryRequest queryReq = new CompanyInfoQueryRequest();
		KsBeanUtil.copyPropertiesThird(companyInfoPageReq, queryReq);
		Page<CompanyInfo> companyInfoPage = companyInfoService.page(queryReq);
		Page<CompanyInfoVO> newPage = companyInfoPage.map(entity -> companyInfoService.wrapperVo(entity));
		MicroServicePage<CompanyInfoVO> microPage = new MicroServicePage<>(newPage, companyInfoPageReq.getPageable());
		CompanyInfoPageResponse finalRes = new CompanyInfoPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<CompanyInfoListResponse> list(@RequestBody @Valid CompanyInfoListRequest companyInfoListReq) {
		CompanyInfoQueryRequest queryReq = new CompanyInfoQueryRequest();
		KsBeanUtil.copyPropertiesThird(companyInfoListReq, queryReq);
		List<CompanyInfo> companyInfoList = companyInfoService.list(queryReq);
		List<CompanyInfoVO> newList = companyInfoList.stream().map(entity -> companyInfoService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new CompanyInfoListResponse(newList));
	}

	@Override
	public BaseResponse<CompanyInfoByIdResponse> getById(@RequestBody @Valid CompanyInfoByIdRequest companyInfoByIdRequest) {
		CompanyInfo companyInfo = companyInfoService.getById(companyInfoByIdRequest.getCompanyInfoId());
		return BaseResponse.success(new CompanyInfoByIdResponse(companyInfoService.wrapperVo(companyInfo)));
	}

	@Override
	public BaseResponse<CompanyInfoRopResponse>findCompanyInfos(){
		List<CompanyInfo> companyInfos = companyInfoService.findCompanyInfos();
		return BaseResponse.success(companyInfoService.wrappCompanyInfoRopResponse
				(companyInfoService.wrapperVo(companyInfos.get(0))));
	}
}

