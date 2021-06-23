package com.wanmi.sbc.goods.provider.impl.storetobeevaluate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.storetobeevaluate.StoreTobeEvaluateQueryProvider;
import com.wanmi.sbc.goods.api.request.storetobeevaluate.StoreTobeEvaluateByIdRequest;
import com.wanmi.sbc.goods.api.request.storetobeevaluate.StoreTobeEvaluateListRequest;
import com.wanmi.sbc.goods.api.request.storetobeevaluate.StoreTobeEvaluatePageRequest;
import com.wanmi.sbc.goods.api.request.storetobeevaluate.StoreTobeEvaluateQueryRequest;
import com.wanmi.sbc.goods.api.response.storetobeevaluate.StoreTobeEvaluateByIdResponse;
import com.wanmi.sbc.goods.api.response.storetobeevaluate.StoreTobeEvaluateListResponse;
import com.wanmi.sbc.goods.api.response.storetobeevaluate.StoreTobeEvaluatePageResponse;
import com.wanmi.sbc.goods.bean.vo.StoreTobeEvaluateVO;
import com.wanmi.sbc.goods.storetobeevaluate.model.root.StoreTobeEvaluate;
import com.wanmi.sbc.goods.storetobeevaluate.service.StoreTobeEvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>店铺服务待评价查询服务接口实现</p>
 * @author lzw
 * @date 2019-03-20 17:01:46
 */
@RestController
@Validated
public class StoreTobeEvaluateQueryController implements StoreTobeEvaluateQueryProvider {
	@Autowired
	private StoreTobeEvaluateService storeTobeEvaluateService;

	@Override
	public BaseResponse<StoreTobeEvaluatePageResponse> page(@RequestBody @Valid StoreTobeEvaluatePageRequest storeTobeEvaluatePageReq) {
		StoreTobeEvaluateQueryRequest queryReq = new StoreTobeEvaluateQueryRequest();
		KsBeanUtil.copyPropertiesThird(storeTobeEvaluatePageReq, queryReq);
		Page<StoreTobeEvaluate> storeTobeEvaluatePage = storeTobeEvaluateService.page(queryReq);
		Page<StoreTobeEvaluateVO> newPage = storeTobeEvaluatePage.map(entity -> storeTobeEvaluateService.wrapperVo(entity));
		MicroServicePage<StoreTobeEvaluateVO> microPage = new MicroServicePage<>(newPage, storeTobeEvaluatePageReq.getPageable());
		StoreTobeEvaluatePageResponse finalRes = new StoreTobeEvaluatePageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<StoreTobeEvaluateListResponse> list(@RequestBody @Valid StoreTobeEvaluateListRequest storeTobeEvaluateListReq) {
		StoreTobeEvaluateQueryRequest queryReq = new StoreTobeEvaluateQueryRequest();
		KsBeanUtil.copyPropertiesThird(storeTobeEvaluateListReq, queryReq);
		List<StoreTobeEvaluate> storeTobeEvaluateList = storeTobeEvaluateService.list(queryReq);
		List<StoreTobeEvaluateVO> newList = storeTobeEvaluateList.stream().map(entity -> storeTobeEvaluateService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new StoreTobeEvaluateListResponse(newList));
	}

	@Override
	public BaseResponse<StoreTobeEvaluateByIdResponse> getById(@RequestBody @Valid StoreTobeEvaluateByIdRequest storeTobeEvaluateByIdRequest) {
		StoreTobeEvaluate storeTobeEvaluate = storeTobeEvaluateService.getById(storeTobeEvaluateByIdRequest.getId());
		return BaseResponse.success(new StoreTobeEvaluateByIdResponse(storeTobeEvaluateService.wrapperVo(storeTobeEvaluate)));
	}

	@Override
	public BaseResponse<Long> getStoreTobeEvaluateNum(@RequestBody StoreTobeEvaluateQueryRequest queryReq){
		return BaseResponse.success(storeTobeEvaluateService.getStoreTobeEvaluateNum(queryReq));
	}

	@Override
	public BaseResponse autoStoreEvaluate() {
		storeTobeEvaluateService.autoStoreEvaluate();
		return BaseResponse.SUCCESSFUL();
	}

}

