package com.wanmi.sbc.customer.provider.impl.livecompany;

import com.wanmi.sbc.customer.api.request.livecompany.*;
import com.wanmi.sbc.customer.api.response.livecompany.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.livecompany.LiveCompanyQueryProvider;
import com.wanmi.sbc.customer.bean.vo.LiveCompanyVO;
import com.wanmi.sbc.customer.livecompany.service.LiveCompanyService;
import com.wanmi.sbc.customer.livecompany.model.root.LiveCompany;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>直播商家查询服务接口实现</p>
 * @author zwb
 * @date 2020-06-06 18:06:59
 */
@RestController
@Validated
public class LiveCompanyQueryController implements LiveCompanyQueryProvider {
	@Autowired
	private LiveCompanyService liveCompanyService;

	@Override
	public BaseResponse<LiveCompanyPageResponse> page(@RequestBody @Valid LiveCompanyPageRequest liveCompanyPageReq) {
		LiveCompanyQueryRequest queryReq = KsBeanUtil.convert(liveCompanyPageReq, LiveCompanyQueryRequest.class);
		Page<LiveCompany> liveCompanyPage = liveCompanyService.page(queryReq);
		Page<LiveCompanyVO> newPage = liveCompanyPage.map(entity -> liveCompanyService.wrapperVo(entity));
		MicroServicePage<LiveCompanyVO> microPage = new MicroServicePage<>(newPage, liveCompanyPageReq.getPageable());
		LiveCompanyPageResponse finalRes = new LiveCompanyPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<LiveCompanyListResponse> list(@RequestBody @Valid LiveCompanyListRequest liveCompanyListReq) {
		LiveCompanyQueryRequest queryReq = KsBeanUtil.convert(liveCompanyListReq, LiveCompanyQueryRequest.class);
		List<LiveCompany> liveCompanyList = liveCompanyService.list(queryReq);
		List<LiveCompanyVO> newList = liveCompanyList.stream().map(entity -> liveCompanyService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new LiveCompanyListResponse(newList));
	}

	@Override
	public BaseResponse<LiveCompanyByIdResponse> getById(@RequestBody @Valid LiveCompanyByIdRequest liveCompanyByIdRequest) {
		LiveCompany liveCompany =
		liveCompanyService.getOne(liveCompanyByIdRequest.getStoreId());

		return BaseResponse.success(new LiveCompanyByIdResponse(liveCompanyService.wrapperVo(liveCompany)));
	}

	@Override
	public BaseResponse<LiveCompanyPagePackResponse> pageNew(@Valid LiveCompanyQueryRequest request) {
		LiveCompanyPagePackResponse response = liveCompanyService.pageNew(request);
		return BaseResponse.success(response);
	}

}

