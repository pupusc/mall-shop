package com.wanmi.sbc.message.provider.impl.pushcustomerenable;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.pushcustomerenable.PushCustomerEnableQueryProvider;
import com.wanmi.sbc.message.api.request.pushcustomerenable.PushCustomerEnableByIdRequest;
import com.wanmi.sbc.message.api.request.pushcustomerenable.PushCustomerEnableListRequest;
import com.wanmi.sbc.message.api.request.pushcustomerenable.PushCustomerEnablePageRequest;
import com.wanmi.sbc.message.api.request.pushcustomerenable.PushCustomerEnableQueryRequest;
import com.wanmi.sbc.message.api.response.pushcustomerenable.PushCustomerEnableByIdResponse;
import com.wanmi.sbc.message.api.response.pushcustomerenable.PushCustomerEnableListResponse;
import com.wanmi.sbc.message.api.response.pushcustomerenable.PushCustomerEnablePageResponse;
import com.wanmi.sbc.message.bean.vo.PushCustomerEnableVO;
import com.wanmi.sbc.message.pushcustomerenable.model.root.PushCustomerEnable;
import com.wanmi.sbc.message.pushcustomerenable.service.PushCustomerEnableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>会员推送通知开关查询服务接口实现</p>
 * @author Bob
 * @date 2020-01-07 15:31:47
 */
@RestController
@Validated
public class PushCustomerEnableQueryController implements PushCustomerEnableQueryProvider {
	@Autowired
	private PushCustomerEnableService pushCustomerEnableService;

	@Override
	public BaseResponse<PushCustomerEnablePageResponse> page(@RequestBody @Valid PushCustomerEnablePageRequest pushCustomerEnablePageReq) {
		PushCustomerEnableQueryRequest queryReq = KsBeanUtil.convert(pushCustomerEnablePageReq, PushCustomerEnableQueryRequest.class);
		Page<PushCustomerEnable> pushCustomerEnablePage = pushCustomerEnableService.page(queryReq);
		Page<PushCustomerEnableVO> newPage = pushCustomerEnablePage.map(entity -> pushCustomerEnableService.wrapperVo(entity));
		MicroServicePage<PushCustomerEnableVO> microPage = new MicroServicePage<>(newPage, pushCustomerEnablePageReq.getPageable());
		PushCustomerEnablePageResponse finalRes = new PushCustomerEnablePageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<PushCustomerEnableListResponse> list(@RequestBody @Valid PushCustomerEnableListRequest pushCustomerEnableListReq) {
		PushCustomerEnableQueryRequest queryReq = KsBeanUtil.convert(pushCustomerEnableListReq, PushCustomerEnableQueryRequest.class);
		List<PushCustomerEnable> pushCustomerEnableList = pushCustomerEnableService.list(queryReq);
		List<PushCustomerEnableVO> newList = pushCustomerEnableList.stream().map(entity -> pushCustomerEnableService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new PushCustomerEnableListResponse(newList));
	}

	@Override
	public BaseResponse<PushCustomerEnableByIdResponse> getById(@RequestBody @Valid PushCustomerEnableByIdRequest pushCustomerEnableByIdRequest) {
		PushCustomerEnable pushCustomerEnable =
		pushCustomerEnableService.getOne(pushCustomerEnableByIdRequest.getCustomerId());
		return BaseResponse.success(new PushCustomerEnableByIdResponse(pushCustomerEnableService.wrapperVo(pushCustomerEnable)));
	}

}

