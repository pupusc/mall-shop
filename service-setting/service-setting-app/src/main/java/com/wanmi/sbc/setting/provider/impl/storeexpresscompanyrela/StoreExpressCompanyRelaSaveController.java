package com.wanmi.sbc.setting.provider.impl.storeexpresscompanyrela;

import com.wanmi.sbc.setting.api.response.storeexpresscompanyrela.StoreExpressRelaRopResponse;
import com.wanmi.sbc.setting.bean.vo.StoreExpressCompanyRelaVO;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.storeexpresscompanyrela.StoreExpressCompanyRelaSaveProvider;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaAddRequest;
import com.wanmi.sbc.setting.api.response.storeexpresscompanyrela.StoreExpressCompanyRelaAddResponse;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaModifyRequest;
import com.wanmi.sbc.setting.api.response.storeexpresscompanyrela.StoreExpressCompanyRelaModifyResponse;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaDelByIdRequest;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaDelByIdListRequest;
import com.wanmi.sbc.setting.storeexpresscompanyrela.service.StoreExpressCompanyRelaService;
import com.wanmi.sbc.setting.storeexpresscompanyrela.model.root.StoreExpressCompanyRela;
import javax.validation.Valid;

/**
 * <p>店铺快递公司关联表保存服务接口实现</p>
 * @author lq
 * @date 2019-11-05 16:12:13
 */
@RestController
@Validated
public class StoreExpressCompanyRelaSaveController implements StoreExpressCompanyRelaSaveProvider {
	@Autowired
	private StoreExpressCompanyRelaService storeExpressCompanyRelaService;

	@Override
	public BaseResponse<StoreExpressRelaRopResponse> add(@RequestBody @Valid StoreExpressCompanyRelaAddRequest storeExpressCompanyRelaAddRequest) {
		StoreExpressCompanyRela storeExpressCompanyRela = new StoreExpressCompanyRela();
		KsBeanUtil.copyPropertiesThird(storeExpressCompanyRelaAddRequest, storeExpressCompanyRela);
		StoreExpressCompanyRelaVO storeExpressCompanyRelaVO = storeExpressCompanyRelaService.wrapperVo(storeExpressCompanyRelaService.add(storeExpressCompanyRela));
		return BaseResponse.success(new StoreExpressRelaRopResponse().builder()
				.companyInfoId(storeExpressCompanyRela.getExpressCompanyId())
				.id(storeExpressCompanyRelaVO.getId())
				.storeId(storeExpressCompanyRela.getStoreId())
				.expressCompanyId(storeExpressCompanyRela.getExpressCompanyId())
		        .build());
	}

	@Override
	public BaseResponse<StoreExpressCompanyRelaModifyResponse> modify(@RequestBody @Valid StoreExpressCompanyRelaModifyRequest storeExpressCompanyRelaModifyRequest) {
		StoreExpressCompanyRela storeExpressCompanyRela = new StoreExpressCompanyRela();
		KsBeanUtil.copyPropertiesThird(storeExpressCompanyRelaModifyRequest, storeExpressCompanyRela);
		return BaseResponse.success(new StoreExpressCompanyRelaModifyResponse(
				storeExpressCompanyRelaService.wrapperVo(storeExpressCompanyRelaService.modify(storeExpressCompanyRela))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid StoreExpressCompanyRelaDelByIdRequest storeExpressCompanyRelaDelByIdRequest) {
		storeExpressCompanyRelaService.deleteById(storeExpressCompanyRelaDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid StoreExpressCompanyRelaDelByIdListRequest storeExpressCompanyRelaDelByIdListRequest) {
		storeExpressCompanyRelaService.deleteByIdList(storeExpressCompanyRelaDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

