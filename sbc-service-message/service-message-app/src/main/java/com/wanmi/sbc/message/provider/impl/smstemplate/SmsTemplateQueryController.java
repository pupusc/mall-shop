package com.wanmi.sbc.message.provider.impl.smstemplate;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.smstemplate.SmsTemplateQueryProvider;
import com.wanmi.sbc.message.api.request.smstemplate.SmsTemplatePageRequest;
import com.wanmi.sbc.message.api.request.smstemplate.SmsTemplateQueryRequest;
import com.wanmi.sbc.message.api.response.smstemplate.SmsTemplatePageResponse;
import com.wanmi.sbc.message.api.request.smstemplate.SmsTemplateListRequest;
import com.wanmi.sbc.message.api.response.smstemplate.SmsTemplateListResponse;
import com.wanmi.sbc.message.api.request.smstemplate.SmsTemplateByIdRequest;
import com.wanmi.sbc.message.api.response.smstemplate.SmsTemplateByIdResponse;
import com.wanmi.sbc.message.bean.vo.SmsTemplateVO;
import com.wanmi.sbc.message.smstemplate.service.SmsTemplateService;
import com.wanmi.sbc.message.smstemplate.model.root.SmsTemplate;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>短信模板查询服务接口实现</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:43:29
 */
@RestController
@Validated
public class SmsTemplateQueryController implements SmsTemplateQueryProvider {
	@Autowired
	private SmsTemplateService smsTemplateService;

	@Override
	public BaseResponse<SmsTemplatePageResponse> page(@RequestBody @Valid SmsTemplatePageRequest smsTemplatePageReq) {
		SmsTemplateQueryRequest queryReq = new SmsTemplateQueryRequest();
		KsBeanUtil.copyPropertiesThird(smsTemplatePageReq, queryReq);
		Page<SmsTemplate> smsTemplatePage = smsTemplateService.page(queryReq);
		Page<SmsTemplateVO> newPage = smsTemplatePage.map(entity -> smsTemplateService.wrapperVo(entity));
		MicroServicePage<SmsTemplateVO> microPage = new MicroServicePage<>(newPage, smsTemplatePageReq.getPageable());
		SmsTemplatePageResponse finalRes = new SmsTemplatePageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<SmsTemplateListResponse> list(@RequestBody @Valid SmsTemplateListRequest smsTemplateListReq) {
		SmsTemplateQueryRequest queryReq = new SmsTemplateQueryRequest();
		KsBeanUtil.copyPropertiesThird(smsTemplateListReq, queryReq);
		List<SmsTemplate> smsTemplateList = smsTemplateService.list(queryReq);
		List<SmsTemplateVO> newList = smsTemplateList.stream().map(entity -> smsTemplateService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new SmsTemplateListResponse(newList));
	}

	@Override
	public BaseResponse<SmsTemplateByIdResponse> getById(@RequestBody @Valid SmsTemplateByIdRequest smsTemplateByIdRequest) {
		SmsTemplate smsTemplate = smsTemplateService.getById(smsTemplateByIdRequest.getId());
		return BaseResponse.success(new SmsTemplateByIdResponse(smsTemplateService.wrapperVo(smsTemplate)));
	}

}

