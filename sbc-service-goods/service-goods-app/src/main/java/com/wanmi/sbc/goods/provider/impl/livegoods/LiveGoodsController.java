package com.wanmi.sbc.goods.provider.impl.livegoods;

import com.wanmi.sbc.goods.api.request.livegoods.*;
import com.wanmi.sbc.goods.api.response.livegoods.*;
import com.wanmi.sbc.goods.bean.vo.LiveGoodsVO;
import com.wanmi.sbc.goods.livegoods.model.root.LiveGoods;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.livegoods.LiveGoodsProvider;
import com.wanmi.sbc.goods.livegoods.service.LiveGoodsService;

import java.util.List;

import javax.validation.Valid;

/**
 * <p>直播商品保存服务接口实现</p>
 * @author zwb
 * @date 2020-06-10 11:05:45
 */
@RestController
@Validated
public class LiveGoodsController implements LiveGoodsProvider {
	@Autowired
	private LiveGoodsService liveGoodsService;

	@Override
	public BaseResponse<LiveGoodsAddResponse> add(@RequestBody @Valid LiveGoodsAddRequest req) {
		liveGoodsService.add(req.getRoomId(),req.getGoodsIdList(),req.getAccessToken());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse<LiveGoodsModifyResponse> modify(@RequestBody @Valid LiveGoodsModifyRequest liveGoodsModifyRequest) {
		LiveGoods liveGoods = KsBeanUtil.convert(liveGoodsModifyRequest, LiveGoods.class);
		return BaseResponse.success(new LiveGoodsModifyResponse(
				liveGoodsService.wrapperVo(liveGoodsService.modify(liveGoods))));
	}

	@Override
	public BaseResponse update(@RequestBody @Valid LiveGoodsUpdateRequest liveGoodsUpdateRequest) {
		liveGoodsService.update(liveGoodsUpdateRequest);
		return  BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid LiveGoodsDelByIdRequest liveGoodsDelByIdRequest) {
		liveGoodsService.deleteById(liveGoodsDelByIdRequest.getId(),liveGoodsDelByIdRequest.getGoodsId(),liveGoodsDelByIdRequest.getAccessToken());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid LiveGoodsDelByIdListRequest liveGoodsDelByIdListRequest) {
		 List<Long> goodsIdList = liveGoodsDelByIdListRequest.getGoodsIdList();
		liveGoodsService.deleteByIdList(goodsIdList);
		return BaseResponse.SUCCESSFUL();
	}


	@Override
	public BaseResponse audit(@RequestBody LiveGoodsAuditRequest liveGoodsAuditRequest) {
		liveGoodsService.audit(liveGoodsAuditRequest);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse<LiveGoodsSupplierAddResponse> supplier(@RequestBody  LiveGoodsSupplierAddRequest supplierAddReq) {
		List<LiveGoodsVO> goodsInfoVOList = supplierAddReq.getGoodsInfoVOList();

		return BaseResponse.success(new LiveGoodsSupplierAddResponse(
				liveGoodsService.supplier(goodsInfoVOList)));
	}

	@Override
	public BaseResponse<LiveGoodsModifyResponse> status(@RequestBody LiveGoodsUpdateRequest updateRequest) {
		LiveGoods liveGoods = KsBeanUtil.convert(updateRequest, LiveGoods.class);
		return BaseResponse.success(new LiveGoodsModifyResponse(
				liveGoodsService.wrapperVo(liveGoodsService.status(liveGoods))));
	}

}

