package com.wanmi.sbc.goods.provider.impl.thirdgoodscate;

import com.wanmi.sbc.goods.api.provider.thirdgoodscate.ThirdGoodsCateProvider;
import com.wanmi.sbc.goods.api.request.thirdgoodscate.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.api.response.thirdgoodscate.ThirdGoodsCateAddResponse;
import com.wanmi.sbc.goods.api.response.thirdgoodscate.ThirdGoodsCateModifyResponse;
import com.wanmi.sbc.goods.thirdgoodscate.service.ThirdGoodsCateService;
import com.wanmi.sbc.goods.thirdgoodscate.model.root.ThirdGoodsCate;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

/**
 * <p>保存服务接口实现</p>
 * @author 
 * @date 2020-08-17 14:46:43
 */
@RestController
@Validated
public class ThirdGoodsCateController implements ThirdGoodsCateProvider {
	@Autowired
	private ThirdGoodsCateService thirdGoodsCateService;

	@Override
	public BaseResponse<ThirdGoodsCateAddResponse> add(@RequestBody @Valid ThirdGoodsCateAddRequest thirdGoodsCateAddRequest) {
		ThirdGoodsCate thirdGoodsCate = KsBeanUtil.convert(thirdGoodsCateAddRequest, ThirdGoodsCate.class);
		return BaseResponse.success(new ThirdGoodsCateAddResponse(
				thirdGoodsCateService.wrapperVo(thirdGoodsCateService.add(thirdGoodsCate))));
	}

	@Override
	public BaseResponse<ThirdGoodsCateModifyResponse> modify(@RequestBody @Valid ThirdGoodsCateModifyRequest thirdGoodsCateModifyRequest) {
		ThirdGoodsCate thirdGoodsCate = KsBeanUtil.convert(thirdGoodsCateModifyRequest, ThirdGoodsCate.class);
		return BaseResponse.success(new ThirdGoodsCateModifyResponse(
				thirdGoodsCateService.wrapperVo(thirdGoodsCateService.modify(thirdGoodsCate))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid ThirdGoodsCateDelByIdRequest thirdGoodsCateDelByIdRequest) {
		ThirdGoodsCate thirdGoodsCate = KsBeanUtil.convert(thirdGoodsCateDelByIdRequest, ThirdGoodsCate.class);
		thirdGoodsCate.setDelFlag(DeleteFlag.YES);
		thirdGoodsCateService.deleteById(thirdGoodsCate);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid ThirdGoodsCateDelByIdListRequest thirdGoodsCateDelByIdListRequest) {
		List<ThirdGoodsCate> thirdGoodsCateList = thirdGoodsCateDelByIdListRequest.getIdList().stream()
			.map(CateId -> {
				ThirdGoodsCate thirdGoodsCate = KsBeanUtil.convert(CateId, ThirdGoodsCate.class);
				thirdGoodsCate.setDelFlag(DeleteFlag.YES);
				return thirdGoodsCate;
			}).collect(Collectors.toList());
		thirdGoodsCateService.deleteByIdList(thirdGoodsCateList);
		return BaseResponse.SUCCESSFUL();
	}

	/**
	 * 全量更新所有类目
	 * @return
	 */
	@Override
	public BaseResponse updateAll(@RequestBody @Valid UpdateAllRequest request) {
		thirdGoodsCateService.updateAll(request.getThirdGoodsCateDTOS());
		return BaseResponse.SUCCESSFUL();
	}

}

