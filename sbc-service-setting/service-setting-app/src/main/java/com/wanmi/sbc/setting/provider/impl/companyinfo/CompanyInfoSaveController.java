package com.wanmi.sbc.setting.provider.impl.companyinfo;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.companyinfo.CompanyInfoSaveProvider;
import com.wanmi.sbc.setting.api.request.companyinfo.CompanyInfoAddRequest;
import com.wanmi.sbc.setting.api.response.companyinfo.CompanyInfoAddResponse;
import com.wanmi.sbc.setting.api.request.companyinfo.CompanyInfoModifyRequest;
import com.wanmi.sbc.setting.api.response.companyinfo.CompanyInfoModifyResponse;
import com.wanmi.sbc.setting.api.request.companyinfo.CompanyInfoDelByIdRequest;
import com.wanmi.sbc.setting.api.request.companyinfo.CompanyInfoDelByIdListRequest;
import com.wanmi.sbc.setting.companyinfo.service.CompanyInfoService;
import com.wanmi.sbc.setting.companyinfo.model.root.CompanyInfo;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>公司信息保存服务接口实现</p>
 * @author lq
 * @date 2019-11-05 16:09:36
 */
@RestController
@Validated
public class CompanyInfoSaveController implements CompanyInfoSaveProvider {
	@Autowired
	private CompanyInfoService companyInfoService;

	@Override
	public BaseResponse<CompanyInfoAddResponse> add(@RequestBody @Valid CompanyInfoAddRequest companyInfoAddRequest) {
		CompanyInfo companyInfo = new CompanyInfo();
		KsBeanUtil.copyPropertiesThird(companyInfoAddRequest, companyInfo);
		return BaseResponse.success(new CompanyInfoAddResponse(
				companyInfoService.wrapperVo(companyInfoService.add(companyInfo))));
	}

	@Override
	public BaseResponse<CompanyInfoModifyResponse> modify(@RequestBody @Valid CompanyInfoModifyRequest companyInfoModifyRequest) {
		CompanyInfo companyInfo = new CompanyInfo();
		companyInfoModifyRequest.setUpdateTime(LocalDateTime.now());
		CompanyInfo companyInfo1 = companyInfoService.getById(companyInfoModifyRequest.getCompanyInfoId());
		if(Objects.isNull(companyInfo1)){
			throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
		}
		companyInfoModifyRequest.setCreateTime(companyInfo1.getCreateTime());
		KsBeanUtil.copyPropertiesThird(companyInfoModifyRequest, companyInfo);
		return BaseResponse.success(new CompanyInfoModifyResponse(
				companyInfoService.wrapperVo(companyInfoService.modify(companyInfo))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid CompanyInfoDelByIdRequest companyInfoDelByIdRequest) {
		companyInfoService.deleteById(companyInfoDelByIdRequest.getCompanyInfoId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid CompanyInfoDelByIdListRequest companyInfoDelByIdListRequest) {
		companyInfoService.deleteByIdList(companyInfoDelByIdListRequest.getCompanyInfoIdList());
		return BaseResponse.SUCCESSFUL();
	}
}

