package com.wanmi.sbc.setting.provider.impl.expresscompany;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.expresscompany.ExpressCompanySaveProvider;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyAddRequest;
import com.wanmi.sbc.setting.api.response.expresscompany.ExpressCompanyAddResponse;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyModifyRequest;
import com.wanmi.sbc.setting.api.response.expresscompany.ExpressCompanyModifyResponse;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyDelByIdRequest;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyDelByIdListRequest;
import com.wanmi.sbc.setting.expresscompany.service.ExpressCompanyService;
import com.wanmi.sbc.setting.expresscompany.model.root.ExpressCompany;
import javax.validation.Valid;

/**
 * <p>物流公司保存服务接口实现</p>
 * @author lq
 * @date 2019-11-05 16:10:00
 */
@RestController
@Validated
public class ExpressCompanySaveController implements ExpressCompanySaveProvider {
	@Autowired
	private ExpressCompanyService expressCompanyService;

	@Override
	public BaseResponse<ExpressCompanyAddResponse> add(@RequestBody @Valid ExpressCompanyAddRequest expressCompanyAddRequest) {
		ExpressCompany expressCompany = new ExpressCompany();
		expressCompany.setIsAdd(1);
		KsBeanUtil.copyPropertiesThird(expressCompanyAddRequest, expressCompany);
		return BaseResponse.success(new ExpressCompanyAddResponse(
				expressCompanyService.wrapperVo(expressCompanyService.add(expressCompany))));
	}

	@Override
	public BaseResponse<ExpressCompanyModifyResponse> modify(@RequestBody @Valid ExpressCompanyModifyRequest expressCompanyModifyRequest) {
		ExpressCompany expressCompany = new ExpressCompany();
		KsBeanUtil.copyPropertiesThird(expressCompanyModifyRequest, expressCompany);
		return BaseResponse.success(new ExpressCompanyModifyResponse(
				expressCompanyService.wrapperVo(expressCompanyService.modify(expressCompany))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid ExpressCompanyDelByIdRequest expressCompanyDelByIdRequest) {
		expressCompanyService.deleteById(expressCompanyDelByIdRequest.getExpressCompanyId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid ExpressCompanyDelByIdListRequest expressCompanyDelByIdListRequest) {
		expressCompanyService.deleteByIdList(expressCompanyDelByIdListRequest.getExpressCompanyIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

