package com.wanmi.sbc.customer.provider.impl.storeevaluate;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.request.storeevaluate.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.storeevaluate.StoreEvaluateSaveProvider;
import com.wanmi.sbc.customer.api.response.storeevaluate.StoreEvaluateAddResponse;
import com.wanmi.sbc.customer.api.response.storeevaluate.StoreEvaluateModifyResponse;
import com.wanmi.sbc.customer.storeevaluate.service.StoreEvaluateService;
import com.wanmi.sbc.customer.storeevaluate.model.root.StoreEvaluate;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>店铺评价保存服务接口实现</p>
 * @author liutao
 * @date 2019-02-26 10:23:32
 */
@RestController
@Validated
public class StoreEvaluateSaveController implements StoreEvaluateSaveProvider {
	@Autowired
	private StoreEvaluateService storeEvaluateService;

	@Override
	public BaseResponse<StoreEvaluateAddResponse> add(@RequestBody @Valid StoreEvaluateAddRequest storeEvaluateAddRequest) {
		StoreEvaluate storeEvaluate = new StoreEvaluate();
		KsBeanUtil.copyPropertiesThird(storeEvaluateAddRequest, storeEvaluate);
		return BaseResponse.success(new StoreEvaluateAddResponse(
				storeEvaluateService.wrapperVo(storeEvaluateService.add(storeEvaluate))));
	}

	@Override
	public BaseResponse addList(@RequestBody StoreEvaluateAddListRequest storeEvaluateAddListRequest){
		List<StoreEvaluate> storeEvaluateList = new ArrayList<>();
		storeEvaluateAddListRequest.getStoreEvaluateAddRequestList().forEach(storeEvaluateAddRequest -> {
			StoreEvaluate storeEvaluate = new StoreEvaluate();
			KsBeanUtil.copyPropertiesThird(storeEvaluateAddRequest, storeEvaluate);
			storeEvaluate.setDelFlag(DeleteFlag.NO.toValue());
			storeEvaluateList.add(storeEvaluate);
		});
		storeEvaluateService.addList(storeEvaluateList);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse<StoreEvaluateModifyResponse> modify(@RequestBody @Valid StoreEvaluateModifyRequest storeEvaluateModifyRequest) {
		StoreEvaluate storeEvaluate = new StoreEvaluate();
		KsBeanUtil.copyPropertiesThird(storeEvaluateModifyRequest, storeEvaluate);
		return BaseResponse.success(new StoreEvaluateModifyResponse(
				storeEvaluateService.wrapperVo(storeEvaluateService.modify(storeEvaluate))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid StoreEvaluateDelByIdRequest storeEvaluateDelByIdRequest) {
		storeEvaluateService.deleteById(storeEvaluateDelByIdRequest.getEvaluateId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid StoreEvaluateDelByIdListRequest storeEvaluateDelByIdListRequest) {
		storeEvaluateService.deleteByIdList(storeEvaluateDelByIdListRequest.getEvaluateIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

