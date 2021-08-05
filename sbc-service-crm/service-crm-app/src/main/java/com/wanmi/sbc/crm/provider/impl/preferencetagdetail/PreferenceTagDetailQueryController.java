package com.wanmi.sbc.crm.provider.impl.preferencetagdetail;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.preferencetagdetail.PreferenceTagDetailQueryProvider;
import com.wanmi.sbc.crm.api.request.preferencetagdetail.PreferenceTagDetailPageRequest;
import com.wanmi.sbc.crm.api.request.preferencetagdetail.PreferenceTagDetailQueryRequest;
import com.wanmi.sbc.crm.api.response.preferencetagdetail.PreferenceTagDetailPageResponse;
import com.wanmi.sbc.crm.api.request.preferencetagdetail.PreferenceTagDetailListRequest;
import com.wanmi.sbc.crm.api.response.preferencetagdetail.PreferenceTagDetailListResponse;
import com.wanmi.sbc.crm.api.request.preferencetagdetail.PreferenceTagDetailByIdRequest;
import com.wanmi.sbc.crm.api.response.preferencetagdetail.PreferenceTagDetailByIdResponse;
import com.wanmi.sbc.crm.bean.vo.PreferenceTagDetailVO;
import com.wanmi.sbc.crm.preferencetagdetail.service.PreferenceTagDetailService;
import com.wanmi.sbc.crm.preferencetagdetail.model.root.PreferenceTagDetail;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>偏好标签明细查询服务接口实现</p>
 * @author dyt
 * @date 2020-03-11 14:58:07
 */
@RestController
@Validated
public class PreferenceTagDetailQueryController implements PreferenceTagDetailQueryProvider {
	@Autowired
	private PreferenceTagDetailService preferenceTagDetailService;

	@Override
	public BaseResponse<PreferenceTagDetailPageResponse> page(@RequestBody @Valid PreferenceTagDetailPageRequest preferenceTagDetailPageReq) {
		PreferenceTagDetailQueryRequest queryReq = KsBeanUtil.convert(preferenceTagDetailPageReq, PreferenceTagDetailQueryRequest.class);
		Page<PreferenceTagDetail> preferenceTagDetailPage = preferenceTagDetailService.page(queryReq);
		Page<PreferenceTagDetailVO> newPage = preferenceTagDetailPage.map(entity -> preferenceTagDetailService.wrapperVo(entity));
		MicroServicePage<PreferenceTagDetailVO> microPage = new MicroServicePage<>(newPage, preferenceTagDetailPageReq.getPageable());
		PreferenceTagDetailPageResponse finalRes = new PreferenceTagDetailPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<PreferenceTagDetailListResponse> list(@RequestBody @Valid PreferenceTagDetailListRequest preferenceTagDetailListReq) {
		PreferenceTagDetailQueryRequest queryReq = KsBeanUtil.convert(preferenceTagDetailListReq, PreferenceTagDetailQueryRequest.class);
		List<PreferenceTagDetail> preferenceTagDetailList = preferenceTagDetailService.list(queryReq);
		List<PreferenceTagDetailVO> newList = preferenceTagDetailList.stream().map(entity -> preferenceTagDetailService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new PreferenceTagDetailListResponse(newList));
	}

	@Override
	public BaseResponse<PreferenceTagDetailByIdResponse> getById(@RequestBody @Valid PreferenceTagDetailByIdRequest preferenceTagDetailByIdRequest) {
		PreferenceTagDetail preferenceTagDetail =
		preferenceTagDetailService.getOne(preferenceTagDetailByIdRequest.getId());
		return BaseResponse.success(new PreferenceTagDetailByIdResponse(preferenceTagDetailService.wrapperVo(preferenceTagDetail)));
	}

}

