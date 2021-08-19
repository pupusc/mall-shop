package com.wanmi.sbc.message.provider.impl.pushdetail;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.pushdetail.PushDetailQueryProvider;
import com.wanmi.sbc.message.api.request.pushdetail.PushDetailByIdRequest;
import com.wanmi.sbc.message.api.request.pushdetail.PushDetailListRequest;
import com.wanmi.sbc.message.api.request.pushdetail.PushDetailPageRequest;
import com.wanmi.sbc.message.api.request.pushdetail.PushDetailQueryRequest;
import com.wanmi.sbc.message.api.response.pushdetail.PushDetailByIdResponse;
import com.wanmi.sbc.message.api.response.pushdetail.PushDetailListResponse;
import com.wanmi.sbc.message.api.response.pushdetail.PushDetailPageResponse;
import com.wanmi.sbc.message.bean.vo.PushDetailVO;
import com.wanmi.sbc.message.pushdetail.model.root.PushDetail;
import com.wanmi.sbc.message.pushdetail.service.PushDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>推送详情查询服务接口实现</p>
 * @author Bob
 * @date 2020-01-08 17:16:17
 */
@RestController
@Validated
public class PushDetailQueryController implements PushDetailQueryProvider {
	@Autowired
	private PushDetailService pushDetailService;

	@Override
	public BaseResponse<PushDetailPageResponse> page(@RequestBody @Valid PushDetailPageRequest pushDetailPageReq) {
		PushDetailQueryRequest queryReq = KsBeanUtil.convert(pushDetailPageReq, PushDetailQueryRequest.class);
		Page<PushDetail> pushDetailPage = pushDetailService.page(queryReq);
		Page<PushDetailVO> newPage = pushDetailPage.map(entity -> pushDetailService.wrapperVo(entity));
		MicroServicePage<PushDetailVO> microPage = new MicroServicePage<>(newPage, pushDetailPageReq.getPageable());
		PushDetailPageResponse finalRes = new PushDetailPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<PushDetailListResponse> list(@RequestBody @Valid PushDetailListRequest pushDetailListReq) {
		PushDetailQueryRequest queryReq = KsBeanUtil.convert(pushDetailListReq, PushDetailQueryRequest.class);
		List<PushDetail> pushDetailList = pushDetailService.list(queryReq);
		List<PushDetailVO> newList = pushDetailList.stream().map(entity -> pushDetailService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new PushDetailListResponse(newList));
	}

	@Override
	public BaseResponse<PushDetailByIdResponse> getById(@RequestBody @Valid PushDetailByIdRequest pushDetailByIdRequest) {
		PushDetail pushDetail =
		pushDetailService.getOne(pushDetailByIdRequest.getTaskId());
		return BaseResponse.success(new PushDetailByIdResponse(pushDetailService.wrapperVo(pushDetail)));
	}

}

