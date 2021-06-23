package com.wanmi.sbc.customer.provider.impl.storeevaluatenum;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.storeevaluatenum.StoreEvaluateNumSaveProvider;
import com.wanmi.sbc.customer.api.request.storeevaluatenum.StoreEvaluateNumAddRequest;
import com.wanmi.sbc.customer.api.response.storeevaluatenum.StoreEvaluateNumAddResponse;
import com.wanmi.sbc.customer.api.request.storeevaluatenum.StoreEvaluateNumModifyRequest;
import com.wanmi.sbc.customer.api.response.storeevaluatenum.StoreEvaluateNumModifyResponse;
import com.wanmi.sbc.customer.api.request.storeevaluatenum.StoreEvaluateNumDelByIdRequest;
import com.wanmi.sbc.customer.api.request.storeevaluatenum.StoreEvaluateNumDelByIdListRequest;
import com.wanmi.sbc.customer.storeevaluatenum.service.StoreEvaluateNumService;
import com.wanmi.sbc.customer.storeevaluatenum.model.root.StoreEvaluateNum;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>店铺统计评分等级人数统计保存服务接口实现</p>
 * @author liutao
 * @date 2019-03-04 10:55:28
 */
@RestController
@Validated
public class StoreEvaluateNumSaveController implements StoreEvaluateNumSaveProvider {
	@Autowired
	private StoreEvaluateNumService storeEvaluateNumService;

	@Override
	public BaseResponse<StoreEvaluateNumAddResponse> add(@RequestBody @Valid StoreEvaluateNumAddRequest storeEvaluateNumAddRequest) {
		StoreEvaluateNum storeEvaluateNum = new StoreEvaluateNum();
		KsBeanUtil.copyPropertiesThird(storeEvaluateNumAddRequest, storeEvaluateNum);
		return BaseResponse.success(new StoreEvaluateNumAddResponse(
				storeEvaluateNumService.wrapperVo(storeEvaluateNumService.add(storeEvaluateNum))));
	}

	@Override
	public BaseResponse<StoreEvaluateNumModifyResponse> modify(@RequestBody @Valid StoreEvaluateNumModifyRequest storeEvaluateNumModifyRequest) {
		StoreEvaluateNum storeEvaluateNum = new StoreEvaluateNum();
		KsBeanUtil.copyPropertiesThird(storeEvaluateNumModifyRequest, storeEvaluateNum);
		return BaseResponse.success(new StoreEvaluateNumModifyResponse(
				storeEvaluateNumService.wrapperVo(storeEvaluateNumService.modify(storeEvaluateNum))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid StoreEvaluateNumDelByIdRequest storeEvaluateNumDelByIdRequest) {
		storeEvaluateNumService.deleteById(storeEvaluateNumDelByIdRequest.getNumId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid StoreEvaluateNumDelByIdListRequest storeEvaluateNumDelByIdListRequest) {
		storeEvaluateNumService.deleteByIdList(storeEvaluateNumDelByIdListRequest.getNumIdList());
		return BaseResponse.SUCCESSFUL();
	}


	/**
	 * 批量新增
	 *
	 * @param storeEvaluateNumAddRequests
	 * @return
	 */
	@Override
	public BaseResponse<StoreEvaluateNumAddResponse> addList(@RequestBody List<StoreEvaluateNumAddRequest> storeEvaluateNumAddRequests) {
		if (CollectionUtils.isEmpty(storeEvaluateNumAddRequests)){
			return BaseResponse.SUCCESSFUL();
		}
		storeEvaluateNumAddRequests.stream().forEach(storeEvaluateNumAddRequest -> {
			this.add(storeEvaluateNumAddRequest);
		});
		return BaseResponse.SUCCESSFUL();
	}

	/**
	 * 删除所有
	 *
	 * @return
	 */
	@Override
	public BaseResponse deleteAll() {
		storeEvaluateNumService.deleteAll();
		return BaseResponse.SUCCESSFUL();
	}

}

