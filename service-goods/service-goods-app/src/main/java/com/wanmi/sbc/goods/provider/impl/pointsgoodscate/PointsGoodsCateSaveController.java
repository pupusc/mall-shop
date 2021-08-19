package com.wanmi.sbc.goods.provider.impl.pointsgoodscate;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.constant.PointsGoodsCateErrorCode;
import com.wanmi.sbc.goods.api.request.pointsgoods.PointsGoodsQueryRequest;
import com.wanmi.sbc.goods.api.request.pointsgoodscate.*;
import com.wanmi.sbc.goods.pointsgoods.model.root.PointsGoods;
import com.wanmi.sbc.goods.pointsgoods.service.PointsGoodsService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.pointsgoodscate.PointsGoodsCateSaveProvider;
import com.wanmi.sbc.goods.api.response.pointsgoodscate.PointsGoodsCateAddResponse;
import com.wanmi.sbc.goods.api.response.pointsgoodscate.PointsGoodsCateModifyResponse;
import com.wanmi.sbc.goods.pointsgoodscate.service.PointsGoodsCateService;
import com.wanmi.sbc.goods.pointsgoodscate.model.root.PointsGoodsCate;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>积分商品分类表保存服务接口实现</p>
 * @author yang
 * @date 2019-05-13 09:50:07
 */
@RestController
@Validated
public class PointsGoodsCateSaveController implements PointsGoodsCateSaveProvider {
	@Autowired
	private PointsGoodsCateService pointsGoodsCateService;

	@Autowired
	private PointsGoodsService pointsGoodsService;

	@Override
	public BaseResponse<PointsGoodsCateAddResponse> add(@RequestBody @Valid PointsGoodsCateAddRequest pointsGoodsCateAddRequest) {
		PointsGoodsCate pointsGoodsCate = new PointsGoodsCate();
		KsBeanUtil.copyPropertiesThird(pointsGoodsCateAddRequest, pointsGoodsCate);
		return BaseResponse.success(new PointsGoodsCateAddResponse(
				pointsGoodsCateService.wrapperVo(pointsGoodsCateService.add(pointsGoodsCate))));
	}

	@Override
	public BaseResponse<PointsGoodsCateModifyResponse> modify(@RequestBody @Valid PointsGoodsCateModifyRequest pointsGoodsCateModifyRequest) {
		PointsGoodsCate pointsGoodsCate = new PointsGoodsCate();
		KsBeanUtil.copyPropertiesThird(pointsGoodsCateModifyRequest, pointsGoodsCate);
		return BaseResponse.success(new PointsGoodsCateModifyResponse(
				pointsGoodsCateService.wrapperVo(pointsGoodsCateService.modify(pointsGoodsCate))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid PointsGoodsCateDelByIdRequest pointsGoodsCateDelByIdRequest) {
		List<PointsGoods> pointsGoodsList = pointsGoodsService.list(PointsGoodsQueryRequest.builder()
				.cateId(pointsGoodsCateDelByIdRequest.getCateId())
				.delFlag(DeleteFlag.NO)
				.build());
		if(pointsGoodsList.size() > 0){
			throw new SbcRuntimeException(PointsGoodsCateErrorCode.UNABLED_DELETE);
		}
		pointsGoodsCateService.deleteById(pointsGoodsCateDelByIdRequest.getCateId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse editSort(@RequestBody PointsGoodsCateSortRequest queryRequest) {
		pointsGoodsCateService.editSort(queryRequest);
		return BaseResponse.SUCCESSFUL();
	}
}

