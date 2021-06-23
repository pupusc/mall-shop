package com.wanmi.sbc.order.provider.impl.exceptionoftradepoints;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.order.api.provider.exceptionoftradepoints.ExceptionOfTradePointsProvider;
import com.wanmi.sbc.order.api.request.exceptionoftradepoints.ExceptionOfTradePointsAddRequest;
import com.wanmi.sbc.order.api.response.exceptionoftradepoints.ExceptionOfTradePointsAddResponse;
import com.wanmi.sbc.order.api.request.exceptionoftradepoints.ExceptionOfTradePointsModifyRequest;
import com.wanmi.sbc.order.api.response.exceptionoftradepoints.ExceptionOfTradePointsModifyResponse;
import com.wanmi.sbc.order.api.request.exceptionoftradepoints.ExceptionOfTradePointsDelByIdRequest;
import com.wanmi.sbc.order.api.request.exceptionoftradepoints.ExceptionOfTradePointsDelByIdListRequest;
import com.wanmi.sbc.order.exceptionoftradepoints.service.ExceptionOfTradePointsService;
import com.wanmi.sbc.order.exceptionoftradepoints.model.root.ExceptionOfTradePoints;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

/**
 * <p>积分订单抵扣异常表保存服务接口实现</p>
 * @author caofang
 * @date 2021-03-10 18:54:25
 */
@RestController
@Validated
public class ExceptionOfTradePointsController implements ExceptionOfTradePointsProvider {
	@Autowired
	private ExceptionOfTradePointsService exceptionOfTradePointsService;

	@Override
	public BaseResponse<ExceptionOfTradePointsAddResponse> add(@RequestBody @Valid ExceptionOfTradePointsAddRequest exceptionOfTradePointsAddRequest) {
		ExceptionOfTradePoints exceptionOfTradePoints = KsBeanUtil.convert(exceptionOfTradePointsAddRequest, ExceptionOfTradePoints.class);
		return BaseResponse.success(new ExceptionOfTradePointsAddResponse(
				exceptionOfTradePointsService.wrapperVo(exceptionOfTradePointsService.add(exceptionOfTradePoints))));
	}

	@Override
	public BaseResponse<ExceptionOfTradePointsModifyResponse> modify(@RequestBody @Valid ExceptionOfTradePointsModifyRequest exceptionOfTradePointsModifyRequest) {
		ExceptionOfTradePoints exceptionOfTradePoints = KsBeanUtil.convert(exceptionOfTradePointsModifyRequest, ExceptionOfTradePoints.class);
		return BaseResponse.success(new ExceptionOfTradePointsModifyResponse(
				exceptionOfTradePointsService.wrapperVo(exceptionOfTradePointsService.modify(exceptionOfTradePoints))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid ExceptionOfTradePointsDelByIdRequest exceptionOfTradePointsDelByIdRequest) {
		ExceptionOfTradePoints exceptionOfTradePoints = KsBeanUtil.convert(exceptionOfTradePointsDelByIdRequest, ExceptionOfTradePoints.class);
		exceptionOfTradePoints.setDelFlag(DeleteFlag.YES);
		exceptionOfTradePointsService.deleteById(exceptionOfTradePoints);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid ExceptionOfTradePointsDelByIdListRequest exceptionOfTradePointsDelByIdListRequest) {
		List<ExceptionOfTradePoints> exceptionOfTradePointsList = exceptionOfTradePointsDelByIdListRequest.getIdList().stream()
			.map(Id -> {
				ExceptionOfTradePoints exceptionOfTradePoints = KsBeanUtil.convert(Id, ExceptionOfTradePoints.class);
				exceptionOfTradePoints.setDelFlag(DeleteFlag.YES);
				return exceptionOfTradePoints;
			}).collect(Collectors.toList());
		exceptionOfTradePointsService.deleteByIdList(exceptionOfTradePointsList);
		return BaseResponse.SUCCESSFUL();
	}

}

