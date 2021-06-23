package com.wanmi.sbc.customer.provider.impl.fdpaidcast;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.provider.fdpaidcast.FdPaidCastProvider;
import com.wanmi.sbc.customer.api.request.fdpaidcast.FdPaidCastAddRequest;
import com.wanmi.sbc.customer.api.response.fdpaidcast.FdPaidCastAddResponse;
import com.wanmi.sbc.customer.api.request.fdpaidcast.FdPaidCastModifyRequest;
import com.wanmi.sbc.customer.api.response.fdpaidcast.FdPaidCastModifyResponse;
import com.wanmi.sbc.customer.api.request.fdpaidcast.FdPaidCastDelByIdRequest;
import com.wanmi.sbc.customer.api.request.fdpaidcast.FdPaidCastDelByIdListRequest;
import com.wanmi.sbc.customer.fdpaidcast.service.FdPaidCastService;
import com.wanmi.sbc.customer.fdpaidcast.model.root.FdPaidCast;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

/**
 * <p>樊登付费类型 映射商城付费类型保存服务接口实现</p>
 * @author tzx
 * @date 2021-01-29 14:13:37
 */
@RestController
@Validated
public class FdPaidCastController implements FdPaidCastProvider {
	@Autowired
	private FdPaidCastService fdPaidCastService;

	@Override
	public BaseResponse<FdPaidCastAddResponse> add(@RequestBody @Valid FdPaidCastAddRequest fdPaidCastAddRequest) {
		FdPaidCast fdPaidCast = KsBeanUtil.convert(fdPaidCastAddRequest, FdPaidCast.class);
		return BaseResponse.success(new FdPaidCastAddResponse(
				fdPaidCastService.wrapperVo(fdPaidCastService.add(fdPaidCast))));
	}

	@Override
	public BaseResponse<FdPaidCastModifyResponse> modify(@RequestBody @Valid FdPaidCastModifyRequest fdPaidCastModifyRequest) {
		FdPaidCast fdPaidCast = KsBeanUtil.convert(fdPaidCastModifyRequest, FdPaidCast.class);
		return BaseResponse.success(new FdPaidCastModifyResponse(
				fdPaidCastService.wrapperVo(fdPaidCastService.modify(fdPaidCast))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid FdPaidCastDelByIdRequest fdPaidCastDelByIdRequest) {
		FdPaidCast fdPaidCast = KsBeanUtil.convert(fdPaidCastDelByIdRequest, FdPaidCast.class);
		fdPaidCast.setDelFlag(DeleteFlag.YES);
		fdPaidCastService.deleteById(fdPaidCast);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid FdPaidCastDelByIdListRequest fdPaidCastDelByIdListRequest) {
		List<FdPaidCast> fdPaidCastList = fdPaidCastDelByIdListRequest.getIdList().stream()
			.map(Id -> {
				FdPaidCast fdPaidCast = KsBeanUtil.convert(Id, FdPaidCast.class);
				fdPaidCast.setDelFlag(DeleteFlag.YES);
				return fdPaidCast;
			}).collect(Collectors.toList());
		fdPaidCastService.deleteByIdList(fdPaidCastList);
		return BaseResponse.SUCCESSFUL();
	}

}

