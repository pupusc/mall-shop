package com.wanmi.sbc.goods.provider.impl.restrictedrecord;

import com.wanmi.sbc.goods.api.request.restrictedrecord.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.restrictedrecord.RestrictedRecordSaveProvider;
import com.wanmi.sbc.goods.api.response.restrictedrecord.RestrictedRecordAddResponse;
import com.wanmi.sbc.goods.api.response.restrictedrecord.RestrictedRecordModifyResponse;
import com.wanmi.sbc.goods.restrictedrecord.service.RestrictedRecordService;
import com.wanmi.sbc.goods.restrictedrecord.model.root.RestrictedRecord;
import javax.validation.Valid;

/**
 * <p>限售保存服务接口实现</p>
 * @author 限售记录
 * @date 2020-04-11 15:59:01
 */
@RestController
@Validated
public class RestrictedRecordSaveController implements RestrictedRecordSaveProvider {
	@Autowired
	private RestrictedRecordService restrictedRecordService;

	@Override
	public BaseResponse<RestrictedRecordAddResponse> add(@RequestBody @Valid RestrictedRecordAddRequest restrictedRecordAddRequest) {
		RestrictedRecord restrictedRecord = new RestrictedRecord();
		KsBeanUtil.copyPropertiesThird(restrictedRecordAddRequest, restrictedRecord);
		return BaseResponse.success(new RestrictedRecordAddResponse(
				restrictedRecordService.wrapperVo(restrictedRecordService.add(restrictedRecord))));
	}



	@Override
	public BaseResponse<RestrictedRecordModifyResponse> modify(@RequestBody @Valid RestrictedRecordModifyRequest restrictedRecordModifyRequest) {
		RestrictedRecord restrictedRecord = new RestrictedRecord();
		KsBeanUtil.copyPropertiesThird(restrictedRecordModifyRequest, restrictedRecord);
		return BaseResponse.success(new RestrictedRecordModifyResponse(
				restrictedRecordService.wrapperVo(restrictedRecordService.modify(restrictedRecord))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid RestrictedRecordDelByIdRequest restrictedRecordDelByIdRequest) {
		restrictedRecordService.deleteById(restrictedRecordDelByIdRequest.getRecordId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid RestrictedRecordDelByIdListRequest restrictedRecordDelByIdListRequest) {
		restrictedRecordService.deleteByIdList(restrictedRecordDelByIdListRequest.getRecordIdList());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse batchAdd(@RequestBody @Valid RestrictedRecordBatchAddRequest request) {
		restrictedRecordService.batchSaveAndPlus(request.getRestrictedRecordSimpVOS(),request.getCustomerId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse reduceRestrictedRecord(@RequestBody @Valid RestrictedRecordBatchAddRequest request) {
		restrictedRecordService.batchReducePurseNum(request.getRestrictedRecordSimpVOS(),request.getCustomerId(), request.getOrderTime());
		return BaseResponse.SUCCESSFUL();
	}

}

