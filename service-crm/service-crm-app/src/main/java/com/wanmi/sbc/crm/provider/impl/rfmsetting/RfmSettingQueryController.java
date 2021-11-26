package com.wanmi.sbc.crm.provider.impl.rfmsetting;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.rfmsetting.RfmSettingQueryProvider;
import com.wanmi.sbc.crm.api.request.rfmsetting.RfmSettingByIdRequest;
import com.wanmi.sbc.crm.api.request.rfmsetting.RfmSettingListRequest;
import com.wanmi.sbc.crm.api.request.rfmsetting.RfmSettingPageRequest;
import com.wanmi.sbc.crm.api.request.rfmsetting.RfmSettingQueryRequest;
import com.wanmi.sbc.crm.api.response.rfmsetting.*;
import com.wanmi.sbc.crm.bean.enums.RFMType;
import com.wanmi.sbc.crm.bean.vo.RfmSettingVO;
import com.wanmi.sbc.crm.rfmsetting.model.root.RfmSetting;
import com.wanmi.sbc.crm.rfmsetting.service.RfmSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>rfm参数配置查询服务接口实现</p>
 * @author zhanglingke
 * @date 2019-10-14 14:33:42
 */
@RestController
@Validated
public class RfmSettingQueryController implements RfmSettingQueryProvider {
	@Autowired
	private RfmSettingService rfmSettingService;

	@Override
	public BaseResponse<RfmSettingPageResponse> page(@RequestBody @Valid RfmSettingPageRequest rfmSettingPageReq) {
		RfmSettingQueryRequest queryReq = new RfmSettingQueryRequest();
		KsBeanUtil.copyPropertiesThird(rfmSettingPageReq, queryReq);
		Page<RfmSetting> rfmSettingPage = rfmSettingService.page(queryReq);
		Page<RfmSettingVO> newPage = rfmSettingPage.map(entity -> rfmSettingService.wrapperVo(entity));
		MicroServicePage<RfmSettingVO> microPage = new MicroServicePage<>(newPage, rfmSettingPageReq.getPageable());
		RfmSettingPageResponse finalRes = new RfmSettingPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<RfmSettingListResponse> list(@RequestBody @Valid RfmSettingListRequest rfmSettingListReq) {
		RfmSettingQueryRequest queryReq = new RfmSettingQueryRequest();
		KsBeanUtil.copyPropertiesThird(rfmSettingListReq, queryReq);
		List<RfmSetting> rfmSettingList = rfmSettingService.list(queryReq);
		List<RfmSettingVO> newList = rfmSettingList.stream().map(entity -> rfmSettingService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new RfmSettingListResponse(newList));
	}

	@Override
	public BaseResponse<RfmSettingByIdResponse> getById(@RequestBody @Valid RfmSettingByIdRequest rfmSettingByIdRequest) {
		RfmSetting rfmSetting = rfmSettingService.getById(rfmSettingByIdRequest.getId());
		return BaseResponse.success(new RfmSettingByIdResponse(rfmSettingService.wrapperVo(rfmSetting)));
	}

	@Override
	public BaseResponse<RfmSettingResponse> getRfmSetting() {
		List<RfmSetting> rfmSettings = rfmSettingService.list(RfmSettingQueryRequest.builder().delFlag(DeleteFlag.NO).build());
		Map<RFMType, List<RfmSetting>> collect = rfmSettings.stream().collect(Collectors.groupingBy(i -> i.getType()));
		RfmSettingResponse rfmSettingResponse = RfmSettingResponse.builder()
				.r(pack(collect.get(RFMType.R)))
				.f(pack(collect.get(RFMType.F)))
				.m(pack(collect.get(RFMType.M)))
				.period(collect.get(RFMType.F).get(0).getPeriod()).build();

		return BaseResponse.success(rfmSettingResponse);
	}

	/**
	 * 封装RFM数据
	 * @param rfmSettings
	 * @return
	 */
	private List<RfmSettingRFMElementBaseResponse> pack(List<RfmSetting> rfmSettings){
		return rfmSettings.stream().map(i->{
			RfmSettingRFMElementBaseResponse rfmSettingRFMElementBaseResponse = new RfmSettingRFMElementBaseResponse();
			rfmSettingRFMElementBaseResponse.setParam(i.getParam());
			rfmSettingRFMElementBaseResponse.setScore(i.getScore());
			return rfmSettingRFMElementBaseResponse;
		}).collect(Collectors.toList());
	}

}

