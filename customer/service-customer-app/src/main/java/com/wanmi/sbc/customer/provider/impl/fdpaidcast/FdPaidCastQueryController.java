package com.wanmi.sbc.customer.provider.impl.fdpaidcast;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.fdpaidcast.FdPaidCastQueryProvider;
import com.wanmi.sbc.customer.api.request.fdpaidcast.FdPaidCastPageRequest;
import com.wanmi.sbc.customer.api.request.fdpaidcast.FdPaidCastQueryRequest;
import com.wanmi.sbc.customer.api.response.fdpaidcast.FdPaidCastPageResponse;
import com.wanmi.sbc.customer.api.request.fdpaidcast.FdPaidCastListRequest;
import com.wanmi.sbc.customer.api.response.fdpaidcast.FdPaidCastListResponse;
import com.wanmi.sbc.customer.api.request.fdpaidcast.FdPaidCastByIdRequest;
import com.wanmi.sbc.customer.api.response.fdpaidcast.FdPaidCastByIdResponse;
import com.wanmi.sbc.customer.bean.vo.FdPaidCastVO;
import com.wanmi.sbc.customer.fdpaidcast.service.FdPaidCastService;
import com.wanmi.sbc.customer.fdpaidcast.model.root.FdPaidCast;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>樊登付费类型 映射商城付费类型查询服务接口实现</p>
 * @author tzx
 * @date 2021-01-29 14:13:37
 */
@RestController
@Validated
public class FdPaidCastQueryController implements FdPaidCastQueryProvider {
	@Autowired
	private FdPaidCastService fdPaidCastService;

	@Override
	public BaseResponse<FdPaidCastPageResponse> page(@RequestBody @Valid FdPaidCastPageRequest fdPaidCastPageReq) {
		FdPaidCastQueryRequest queryReq = KsBeanUtil.convert(fdPaidCastPageReq, FdPaidCastQueryRequest.class);
		Page<FdPaidCast> fdPaidCastPage = fdPaidCastService.page(queryReq);
		Page<FdPaidCastVO> newPage = fdPaidCastPage.map(entity -> fdPaidCastService.wrapperVo(entity));
		MicroServicePage<FdPaidCastVO> microPage = new MicroServicePage<>(newPage, fdPaidCastPageReq.getPageable());
		FdPaidCastPageResponse finalRes = new FdPaidCastPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<FdPaidCastListResponse> list(@RequestBody @Valid FdPaidCastListRequest fdPaidCastListReq) {
		FdPaidCastQueryRequest queryReq = KsBeanUtil.convert(fdPaidCastListReq, FdPaidCastQueryRequest.class);
		List<FdPaidCast> fdPaidCastList = fdPaidCastService.list(queryReq);
		List<FdPaidCastVO> newList = fdPaidCastList.stream().map(entity -> fdPaidCastService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new FdPaidCastListResponse(newList));
	}

	@Override
	public BaseResponse<FdPaidCastByIdResponse> getById(@RequestBody @Valid FdPaidCastByIdRequest fdPaidCastByIdRequest) {
		FdPaidCast fdPaidCast =
		fdPaidCastService.getOne(fdPaidCastByIdRequest.getId());
		return BaseResponse.success(new FdPaidCastByIdResponse(fdPaidCastService.wrapperVo(fdPaidCast)));
	}

}

