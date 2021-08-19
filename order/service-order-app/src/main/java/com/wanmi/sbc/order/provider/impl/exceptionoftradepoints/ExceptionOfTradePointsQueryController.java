package com.wanmi.sbc.order.provider.impl.exceptionoftradepoints;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.exceptionoftradepoints.ExceptionOfTradePointsQueryProvider;
import com.wanmi.sbc.order.api.request.exceptionoftradepoints.ExceptionOfTradePointsPageRequest;
import com.wanmi.sbc.order.api.request.exceptionoftradepoints.ExceptionOfTradePointsQueryRequest;
import com.wanmi.sbc.order.api.response.exceptionoftradepoints.ExceptionOfTradePointsPageResponse;
import com.wanmi.sbc.order.api.request.exceptionoftradepoints.ExceptionOfTradePointsListRequest;
import com.wanmi.sbc.order.api.response.exceptionoftradepoints.ExceptionOfTradePointsListResponse;
import com.wanmi.sbc.order.api.request.exceptionoftradepoints.ExceptionOfTradePointsByIdRequest;
import com.wanmi.sbc.order.api.response.exceptionoftradepoints.ExceptionOfTradePointsByIdResponse;
import com.wanmi.sbc.order.bean.vo.ExceptionOfTradePointsVO;
import com.wanmi.sbc.order.exceptionoftradepoints.service.ExceptionOfTradePointsService;
import com.wanmi.sbc.order.exceptionoftradepoints.model.root.ExceptionOfTradePoints;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>积分订单抵扣异常表查询服务接口实现</p>
 * @author caofang
 * @date 2021-03-10 18:54:25
 */
@RestController
@Validated
public class ExceptionOfTradePointsQueryController implements ExceptionOfTradePointsQueryProvider {
	@Autowired
	private ExceptionOfTradePointsService exceptionOfTradePointsService;

	@Override
	public BaseResponse<ExceptionOfTradePointsPageResponse> page(@RequestBody @Valid ExceptionOfTradePointsPageRequest exceptionOfTradePointsPageReq) {
		ExceptionOfTradePointsQueryRequest queryReq = KsBeanUtil.convert(exceptionOfTradePointsPageReq, ExceptionOfTradePointsQueryRequest.class);
		Page<ExceptionOfTradePoints> exceptionOfTradePointsPage = exceptionOfTradePointsService.page(queryReq);
		Page<ExceptionOfTradePointsVO> newPage = exceptionOfTradePointsPage.map(entity -> exceptionOfTradePointsService.wrapperVo(entity));
		MicroServicePage<ExceptionOfTradePointsVO> microPage = new MicroServicePage<>(newPage, exceptionOfTradePointsPageReq.getPageable());
		ExceptionOfTradePointsPageResponse finalRes = new ExceptionOfTradePointsPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<ExceptionOfTradePointsListResponse> list(@RequestBody @Valid ExceptionOfTradePointsListRequest exceptionOfTradePointsListReq) {
		ExceptionOfTradePointsQueryRequest queryReq = KsBeanUtil.convert(exceptionOfTradePointsListReq, ExceptionOfTradePointsQueryRequest.class);
		List<ExceptionOfTradePoints> exceptionOfTradePointsList = exceptionOfTradePointsService.list(queryReq);
		List<ExceptionOfTradePointsVO> newList = exceptionOfTradePointsList.stream().map(entity -> exceptionOfTradePointsService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new ExceptionOfTradePointsListResponse(newList));
	}

	@Override
	public BaseResponse<ExceptionOfTradePointsByIdResponse> getById(@RequestBody @Valid ExceptionOfTradePointsByIdRequest exceptionOfTradePointsByIdRequest) {
		ExceptionOfTradePoints exceptionOfTradePoints =
		exceptionOfTradePointsService.getOne(exceptionOfTradePointsByIdRequest.getId());
		return BaseResponse.success(new ExceptionOfTradePointsByIdResponse(exceptionOfTradePointsService.wrapperVo(exceptionOfTradePoints)));
	}

}

