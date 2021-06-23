package com.wanmi.sbc.customer.provider.impl.storeevaluatesum;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.storeevaluatesum.StoreEvaluateSumSaveProvider;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumAddRequest;
import com.wanmi.sbc.customer.api.response.storeevaluatesum.StoreEvaluateSumAddResponse;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumModifyRequest;
import com.wanmi.sbc.customer.api.response.storeevaluatesum.StoreEvaluateSumModifyResponse;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumDelByIdRequest;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumDelByIdListRequest;
import com.wanmi.sbc.customer.storeevaluatesum.service.StoreEvaluateSumService;
import com.wanmi.sbc.customer.storeevaluatesum.model.root.StoreEvaluateSum;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>店铺评价保存服务接口实现</p>
 * @author liutao
 * @date 2019-02-23 10:59:09
 */
@RestController
@Validated
public class StoreEvaluateSumSaveController implements StoreEvaluateSumSaveProvider {
	@Autowired
	private StoreEvaluateSumService storeEvaluateSumService;

	@Override
	public BaseResponse<StoreEvaluateSumAddResponse> add(@RequestBody @Valid StoreEvaluateSumAddRequest storeEvaluateSumAddRequest) {
		StoreEvaluateSum storeEvaluateSum = new StoreEvaluateSum();
		KsBeanUtil.copyPropertiesThird(storeEvaluateSumAddRequest, storeEvaluateSum);
		return BaseResponse.success(new StoreEvaluateSumAddResponse(
				storeEvaluateSumService.wrapperVo(storeEvaluateSumService.add(storeEvaluateSum))));
	}

	@Override
	public BaseResponse<StoreEvaluateSumModifyResponse> modify(@RequestBody @Valid StoreEvaluateSumModifyRequest storeEvaluateSumModifyRequest) {
		StoreEvaluateSum storeEvaluateSum = new StoreEvaluateSum();
		KsBeanUtil.copyPropertiesThird(storeEvaluateSumModifyRequest, storeEvaluateSum);
		return BaseResponse.success(new StoreEvaluateSumModifyResponse(
				storeEvaluateSumService.wrapperVo(storeEvaluateSumService.modify(storeEvaluateSum))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid StoreEvaluateSumDelByIdRequest storeEvaluateSumDelByIdRequest) {
		storeEvaluateSumService.deleteById(storeEvaluateSumDelByIdRequest.getSumId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid StoreEvaluateSumDelByIdListRequest storeEvaluateSumDelByIdListRequest) {
		storeEvaluateSumService.deleteByIdList(storeEvaluateSumDelByIdListRequest.getSumIdList());
		return BaseResponse.SUCCESSFUL();
	}

	/**
	 * 批量新增
	 *
	 * @param storeEvaluateSumAddRequestList 店铺评价新增参数结构 {@link StoreEvaluateSumAddRequest}
	 * @return
	 */
	@Override
	public BaseResponse<StoreEvaluateSumAddResponse> addList(@RequestBody List<StoreEvaluateSumAddRequest> storeEvaluateSumAddRequestList) {
		if (CollectionUtils.isEmpty(storeEvaluateSumAddRequestList)){
			return BaseResponse.SUCCESSFUL();
		}
		storeEvaluateSumAddRequestList.stream().forEach(storeEvaluateSumAddRequest -> {
			this.add(storeEvaluateSumAddRequest);
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
		storeEvaluateSumService.deleteAll();
		return BaseResponse.SUCCESSFUL();
	}
}

