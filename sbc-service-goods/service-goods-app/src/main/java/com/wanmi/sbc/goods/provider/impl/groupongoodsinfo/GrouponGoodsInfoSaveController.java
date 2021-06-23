package com.wanmi.sbc.goods.provider.impl.groupongoodsinfo;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.groupongoodsinfo.GrouponGoodsInfoSaveProvider;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.*;
import com.wanmi.sbc.goods.groupongoodsinfo.model.root.GrouponGoodsInfo;
import com.wanmi.sbc.goods.groupongoodsinfo.service.GrouponGoodsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>拼团活动商品信息表保存服务接口实现</p>
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@RestController
@Validated
public class GrouponGoodsInfoSaveController implements GrouponGoodsInfoSaveProvider {

	@Autowired
	private GrouponGoodsInfoService grouponGoodsInfoService;

	@Override
	public BaseResponse batchAdd(
			@RequestBody @Valid GrouponGoodsInfoBatchAddRequest request) {
		grouponGoodsInfoService.batchAdd(
				KsBeanUtil.copyListProperties(request.getGoodsInfos(), GrouponGoodsInfo.class));
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse batchEdit(
			@RequestBody @Valid GrouponGoodsInfoBatchEditRequest request) {
		grouponGoodsInfoService.batchEdit(
				KsBeanUtil.copyListProperties(request.getGoodsInfos(), GrouponGoodsInfo.class));
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByGrouponActivityId(
			@RequestBody @Valid GrouponGoodsInfoDeleteByGrouponActivityIdRequest request) {
		Integer result = grouponGoodsInfoService.deleteByGrouponActivityId(request.getGrouponActivityId());
		return result > 0 ? BaseResponse.SUCCESSFUL() : BaseResponse.FAILED();
	}

	@Override
	public BaseResponse modifyReturnInfo(@RequestBody @Valid GrouponGoodsInfoReturnModifyRequest modifyRequest) {
		grouponGoodsInfoService.updateReturnOrderStatisticNumByGrouponActivityIdAndGoodsInfoId(modifyRequest.getGrouponActivityId(),
				modifyRequest.getGoodsInfoId(),modifyRequest.getNum(),modifyRequest.getAmount());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse modifyAuditStatusByGrouponActivityIds(@RequestBody @Valid GrouponGoodsInfoModifyAuditStatusRequest request){
		int result = grouponGoodsInfoService.updateAuditStatusByGrouponActivityIds(request.getGrouponActivityIds(),request.getAuditStatus());
		return BaseResponse.success(result);
	}

	@Override
	public BaseResponse modifyStickyByGrouponActivityIds(@RequestBody @Valid GrouponGoodsInfoModifyStickyRequest request){
		int result = grouponGoodsInfoService.updateStickyByGrouponActivityIds(request.getGrouponActivityIds(),request.getSticky());
		return BaseResponse.success(result);
	}
}

