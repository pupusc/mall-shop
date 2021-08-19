package com.wanmi.sbc.setting.provider.impl.storeexpresscompanyrela;

import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyListRequest;
import com.wanmi.sbc.setting.bean.vo.ExpressCompanyVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.storeexpresscompanyrela.StoreExpressCompanyRelaQueryProvider;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaPageRequest;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaQueryRequest;
import com.wanmi.sbc.setting.api.response.storeexpresscompanyrela.StoreExpressCompanyRelaPageResponse;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaListRequest;
import com.wanmi.sbc.setting.api.response.storeexpresscompanyrela.StoreExpressCompanyRelaListResponse;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaByIdRequest;
import com.wanmi.sbc.setting.api.response.storeexpresscompanyrela.StoreExpressCompanyRelaByIdResponse;
import com.wanmi.sbc.setting.bean.vo.StoreExpressCompanyRelaVO;
import com.wanmi.sbc.setting.storeexpresscompanyrela.service.StoreExpressCompanyRelaService;
import com.wanmi.sbc.setting.storeexpresscompanyrela.model.root.StoreExpressCompanyRela;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>店铺快递公司关联表查询服务接口实现</p>
 * @author lq
 * @date 2019-11-05 16:12:13
 */
@RestController
@Validated
public class StoreExpressCompanyRelaQueryController implements StoreExpressCompanyRelaQueryProvider {
	@Autowired
	private StoreExpressCompanyRelaService storeExpressCompanyRelaService;

	@Override
	public BaseResponse<StoreExpressCompanyRelaPageResponse> page(@RequestBody @Valid StoreExpressCompanyRelaPageRequest storeExpressCompanyRelaPageReq) {
		StoreExpressCompanyRelaQueryRequest queryReq = new StoreExpressCompanyRelaQueryRequest();
		KsBeanUtil.copyPropertiesThird(storeExpressCompanyRelaPageReq, queryReq);
		Page<StoreExpressCompanyRela> storeExpressCompanyRelaPage = storeExpressCompanyRelaService.page(queryReq);
		Page<StoreExpressCompanyRelaVO> newPage = storeExpressCompanyRelaPage.map(entity -> storeExpressCompanyRelaService.wrapperVo(entity));
		MicroServicePage<StoreExpressCompanyRelaVO> microPage = new MicroServicePage<>(newPage, storeExpressCompanyRelaPageReq.getPageable());
		StoreExpressCompanyRelaPageResponse finalRes = new StoreExpressCompanyRelaPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<StoreExpressCompanyRelaListResponse> list(@RequestBody @Valid StoreExpressCompanyRelaListRequest storeExpressCompanyRelaListReq) {
		List<StoreExpressCompanyRela> storeExpressCompanyRelaList = storeExpressCompanyRelaService.list(storeExpressCompanyRelaListReq);
		if(CollectionUtils.isNotEmpty(storeExpressCompanyRelaList)){
			List<StoreExpressCompanyRelaVO> newList = storeExpressCompanyRelaList.stream().map(entity -> storeExpressCompanyRelaService.wrapperVo(entity)).collect(Collectors.toList());
			return BaseResponse.success(new StoreExpressCompanyRelaListResponse(newList));
		}
		return BaseResponse.success(null);
	}

	@Override
	public BaseResponse<StoreExpressCompanyRelaByIdResponse> getById(@RequestBody @Valid StoreExpressCompanyRelaByIdRequest storeExpressCompanyRelaByIdRequest) {
		StoreExpressCompanyRela storeExpressCompanyRela = storeExpressCompanyRelaService.getById(storeExpressCompanyRelaByIdRequest.getId());
		return BaseResponse.success(new StoreExpressCompanyRelaByIdResponse(storeExpressCompanyRelaService.wrapperVo(storeExpressCompanyRela)));
	}

}

