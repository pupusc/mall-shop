package com.wanmi.sbc.setting.provider.impl.storeresourcecate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.storeresourcecate.StoreResourceCateQueryProvider;
import com.wanmi.sbc.setting.api.request.storeresourcecate.*;
import com.wanmi.sbc.setting.api.response.storeresourcecate.StoreResourceCateByIdResponse;
import com.wanmi.sbc.setting.api.response.storeresourcecate.StoreResourceCateListResponse;
import com.wanmi.sbc.setting.api.response.storeresourcecate.StoreResourceCatePageResponse;
import com.wanmi.sbc.setting.bean.vo.StoreResourceCateVO;
import com.wanmi.sbc.setting.storeresourcecate.model.root.StoreResourceCate;
import com.wanmi.sbc.setting.storeresourcecate.service.StoreResourceCateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>店铺资源资源分类表查询服务接口实现</p>
 * @author lq
 * @date 2019-11-05 16:13:19
 */
@RestController
@Validated
public class StoreResourceCateQueryController implements StoreResourceCateQueryProvider {
	@Autowired
	private StoreResourceCateService storeResourceCateService;

	@Override
	public BaseResponse<StoreResourceCatePageResponse> page(@RequestBody @Valid StoreResourceCatePageRequest storeResourceCatePageReq) {
		StoreResourceCateQueryRequest queryReq = new StoreResourceCateQueryRequest();
		KsBeanUtil.copyPropertiesThird(storeResourceCatePageReq, queryReq);
		Page<StoreResourceCate> storeResourceCatePage = storeResourceCateService.page(queryReq);
		Page<StoreResourceCateVO> newPage = storeResourceCatePage.map(entity -> storeResourceCateService.wrapperVo(entity));
		MicroServicePage<StoreResourceCateVO> microPage = new MicroServicePage<>(newPage, storeResourceCatePageReq.getPageable());
		StoreResourceCatePageResponse finalRes = new StoreResourceCatePageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<StoreResourceCateListResponse> list(@RequestBody @Valid StoreResourceCateListRequest storeResourceCateListReq) {
		StoreResourceCateQueryRequest queryReq = new StoreResourceCateQueryRequest();
		KsBeanUtil.copyPropertiesThird(storeResourceCateListReq, queryReq);
		queryReq.putSort("cateId", SortType.ASC.toValue());
		queryReq.putSort("createTime", SortType.DESC.toValue());
		queryReq.putSort("sort", SortType.ASC.toValue());
		queryReq.setDelFlag( DeleteFlag.NO);
		List<StoreResourceCate> storeResourceCateList = storeResourceCateService.list(queryReq);
		List<StoreResourceCateVO> newList = storeResourceCateList.stream().map(entity -> storeResourceCateService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new StoreResourceCateListResponse(newList));
	}

	@Override
	public BaseResponse<StoreResourceCateByIdResponse> getById(@RequestBody @Valid StoreResourceCateByIdRequest storeResourceCateByIdRequest) {
		StoreResourceCate storeResourceCate = storeResourceCateService.getById(storeResourceCateByIdRequest.getCateId());
		return BaseResponse.success(new StoreResourceCateByIdResponse(storeResourceCateService.wrapperVo(storeResourceCate)));
	}

	@Override
	public BaseResponse<Integer> checkChild(@RequestBody @Valid StoreResourceCateCheckChildRequest
													request) {
		return BaseResponse.success(storeResourceCateService.checkChild(request.getCateId(),request.getStoreId()));
	}


	@Override
	public BaseResponse<Integer> checkResource(@RequestBody @Valid  StoreResourceCateCheckResourceRequest
													   request) {
		return BaseResponse.success(storeResourceCateService.checkResource(request.getCateId(),request.getStoreId()));
	}
}

