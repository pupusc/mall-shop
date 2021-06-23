package com.wanmi.sbc.message.provider.impl.smssetting;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.smssetting.SmsSettingQueryProvider;
import com.wanmi.sbc.message.api.request.smssetting.SmsSettingPageRequest;
import com.wanmi.sbc.message.api.request.smssetting.SmsSettingQueryRequest;
import com.wanmi.sbc.message.api.response.smssetting.SmsSettingPageResponse;
import com.wanmi.sbc.message.api.request.smssetting.SmsSettingListRequest;
import com.wanmi.sbc.message.api.response.smssetting.SmsSettingListResponse;
import com.wanmi.sbc.message.api.request.smssetting.SmsSettingByIdRequest;
import com.wanmi.sbc.message.api.response.smssetting.SmsSettingByIdResponse;
import com.wanmi.sbc.message.bean.vo.SmsSettingVO;
import com.wanmi.sbc.message.smssetting.service.SmsSettingService;
import com.wanmi.sbc.message.smssetting.model.root.SmsSetting;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>短信配置查询服务接口实现</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:15:28
 */
@RestController
@Validated
public class SmsSettingQueryController implements SmsSettingQueryProvider {
	@Autowired
	private SmsSettingService smsSettingService;

	@Override
	public BaseResponse<SmsSettingPageResponse> page(@RequestBody @Valid SmsSettingPageRequest smsSettingPageReq) {
		SmsSettingQueryRequest queryReq = new SmsSettingQueryRequest();
		KsBeanUtil.copyPropertiesThird(smsSettingPageReq, queryReq);
		Page<SmsSetting> smsSettingPage = smsSettingService.page(queryReq);
		Page<SmsSettingVO> newPage = smsSettingPage.map(entity -> smsSettingService.wrapperVo(entity));
		MicroServicePage<SmsSettingVO> microPage = new MicroServicePage<>(newPage, smsSettingPageReq.getPageable());
		SmsSettingPageResponse finalRes = new SmsSettingPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<SmsSettingListResponse> list(@RequestBody @Valid SmsSettingListRequest smsSettingListReq) {
		SmsSettingQueryRequest queryReq = new SmsSettingQueryRequest();
		KsBeanUtil.copyPropertiesThird(smsSettingListReq, queryReq);
		List<SmsSetting> smsSettingList = smsSettingService.list(queryReq);
		List<SmsSettingVO> newList = smsSettingList.stream().map(entity -> smsSettingService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new SmsSettingListResponse(newList));
	}

	@Override
	public BaseResponse<SmsSettingByIdResponse> getById(@RequestBody @Valid SmsSettingByIdRequest smsSettingByIdRequest) {
		SmsSetting smsSetting = smsSettingService.getById(smsSettingByIdRequest.getId());
		return BaseResponse.success(new SmsSettingByIdResponse(smsSettingService.wrapperVo(smsSetting)));
	}

}

