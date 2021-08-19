package com.wanmi.sbc.goods.provider.impl.storetobeevaluate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.storetobeevaluate.StoreTobeEvaluateSaveProvider;
import com.wanmi.sbc.goods.api.request.storetobeevaluate.*;
import com.wanmi.sbc.goods.api.response.storetobeevaluate.StoreTobeEvaluateAddResponse;
import com.wanmi.sbc.goods.api.response.storetobeevaluate.StoreTobeEvaluateModifyResponse;
import com.wanmi.sbc.goods.storetobeevaluate.model.root.StoreTobeEvaluate;
import com.wanmi.sbc.goods.storetobeevaluate.service.StoreTobeEvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>店铺服务待评价保存服务接口实现</p>
 * @author lzw
 * @date 2019-03-20 17:01:46
 */
@RestController
@Validated
public class StoreTobeEvaluateSaveController implements StoreTobeEvaluateSaveProvider {
	@Autowired
	private StoreTobeEvaluateService storeTobeEvaluateService;

	@Override
	public BaseResponse<StoreTobeEvaluateAddResponse> add(@RequestBody @Valid StoreTobeEvaluateAddRequest storeTobeEvaluateAddRequest) {
		StoreTobeEvaluate storeTobeEvaluate = new StoreTobeEvaluate();
		KsBeanUtil.copyPropertiesThird(storeTobeEvaluateAddRequest, storeTobeEvaluate);
		return BaseResponse.success(new StoreTobeEvaluateAddResponse(
				storeTobeEvaluateService.wrapperVo(storeTobeEvaluateService.add(storeTobeEvaluate))));
	}

	@Override
	public BaseResponse<StoreTobeEvaluateModifyResponse> modify(@RequestBody @Valid StoreTobeEvaluateModifyRequest storeTobeEvaluateModifyRequest) {
		StoreTobeEvaluate storeTobeEvaluate = new StoreTobeEvaluate();
		KsBeanUtil.copyPropertiesThird(storeTobeEvaluateModifyRequest, storeTobeEvaluate);
		return BaseResponse.success(new StoreTobeEvaluateModifyResponse(
				storeTobeEvaluateService.wrapperVo(storeTobeEvaluateService.modify(storeTobeEvaluate))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid StoreTobeEvaluateDelByIdRequest storeTobeEvaluateDelByIdRequest) {
		storeTobeEvaluateService.deleteById(storeTobeEvaluateDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid StoreTobeEvaluateDelByIdListRequest storeTobeEvaluateDelByIdListRequest) {
		storeTobeEvaluateService.deleteByIdList(storeTobeEvaluateDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

	/**
	 * @param storeTobeEvaluateQueryRequest
	 * @Description: 订单ID和店铺ID删除
	 * @Author: Bob
	 * @Date: 2019-04-12 16:29
	 */
	@Override
	public BaseResponse<Integer> deleteByOrderAndStoreId(@RequestBody @Valid StoreTobeEvaluateQueryRequest storeTobeEvaluateQueryRequest) {
		return BaseResponse.success(storeTobeEvaluateService.delByOrderIDAndStoreID(storeTobeEvaluateQueryRequest));
	}

	/**
	 * @param request
	 * @Description: 动态条件查询
	 * @Author: Bob
	 * @Date: 2019-04-12 17:19
	 */
	@Override
	public BaseResponse<StoreTobeEvaluateAddResponse> query(@RequestBody @Valid StoreTobeEvaluateQueryRequest request) {
		StoreTobeEvaluateAddResponse response =
				StoreTobeEvaluateAddResponse.builder().storeTobeEvaluateVO(storeTobeEvaluateService
						.query(request)).build();
		return BaseResponse.success(response);
	}

}

