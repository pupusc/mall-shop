package com.wanmi.sbc.customer.provider.impl.storeevaluate;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.storeevaluate.StoreEvaluateQueryProvider;
import com.wanmi.sbc.customer.api.request.storeevaluate.StoreEvaluatePageRequest;
import com.wanmi.sbc.customer.api.request.storeevaluate.StoreEvaluateQueryRequest;
import com.wanmi.sbc.customer.api.response.storeevaluate.StoreEvaluatePageResponse;
import com.wanmi.sbc.customer.api.request.storeevaluate.StoreEvaluateListRequest;
import com.wanmi.sbc.customer.api.response.storeevaluate.StoreEvaluateListResponse;
import com.wanmi.sbc.customer.api.request.storeevaluate.StoreEvaluateByIdRequest;
import com.wanmi.sbc.customer.api.response.storeevaluate.StoreEvaluateByIdResponse;
import com.wanmi.sbc.customer.bean.vo.StoreEvaluateVO;
import com.wanmi.sbc.customer.storeevaluate.service.StoreEvaluateService;
import com.wanmi.sbc.customer.storeevaluate.model.root.StoreEvaluate;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>店铺评价查询服务接口实现</p>
 * @author liutao
 * @date 2019-02-26 10:23:32
 */
@RestController
@Validated
public class StoreEvaluateQueryController implements StoreEvaluateQueryProvider {
	@Autowired
	private StoreEvaluateService storeEvaluateService;

	@Override
	public BaseResponse<StoreEvaluatePageResponse> page(@RequestBody @Valid StoreEvaluatePageRequest storeEvaluatePageReq) {
		StoreEvaluateQueryRequest queryReq = new StoreEvaluateQueryRequest();
		KsBeanUtil.copyPropertiesThird(storeEvaluatePageReq, queryReq);
		Page<StoreEvaluate> storeEvaluatePage = storeEvaluateService.page(queryReq);
		Page<StoreEvaluateVO> newPage = storeEvaluatePage.map(entity -> storeEvaluateService.wrapperVo(entity));
		MicroServicePage<StoreEvaluateVO> microPage = new MicroServicePage<>(newPage, storeEvaluatePageReq.getPageable());
		StoreEvaluatePageResponse finalRes = new StoreEvaluatePageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<StoreEvaluateListResponse> list(@RequestBody @Valid StoreEvaluateListRequest storeEvaluateListReq) {
		StoreEvaluateQueryRequest queryReq = new StoreEvaluateQueryRequest();
		KsBeanUtil.copyPropertiesThird(storeEvaluateListReq, queryReq);
		List<StoreEvaluate> storeEvaluateList = storeEvaluateService.list(queryReq);
		List<StoreEvaluateVO> newList = storeEvaluateList.stream().map(entity -> storeEvaluateService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new StoreEvaluateListResponse(newList));
	}

	@Override
	public BaseResponse<StoreEvaluateByIdResponse> getById(@RequestBody @Valid StoreEvaluateByIdRequest storeEvaluateByIdRequest) {
		StoreEvaluate storeEvaluate = storeEvaluateService.getById(storeEvaluateByIdRequest.getEvaluateId());
		return BaseResponse.success(new StoreEvaluateByIdResponse(storeEvaluateService.wrapperVo(storeEvaluate)));
	}

}

