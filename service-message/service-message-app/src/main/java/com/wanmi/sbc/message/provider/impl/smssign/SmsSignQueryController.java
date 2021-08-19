package com.wanmi.sbc.message.provider.impl.smssign;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.message.api.request.smssend.SmsSendQueryRequest;
import com.wanmi.sbc.message.api.request.smssign.*;
import com.wanmi.sbc.message.api.request.smstemplate.SmsTemplateQueryRequest;
import com.wanmi.sbc.message.bean.enums.SendStatus;
import com.wanmi.sbc.message.smssend.service.SmsSendService;
import com.wanmi.sbc.message.smstemplate.service.SmsTemplateService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.smssign.SmsSignQueryProvider;
import com.wanmi.sbc.message.api.response.smssign.SmsSignPageResponse;
import com.wanmi.sbc.message.api.response.smssign.SmsSignListResponse;
import com.wanmi.sbc.message.api.response.smssign.SmsSignByIdResponse;
import com.wanmi.sbc.message.bean.vo.SmsSignVO;
import com.wanmi.sbc.message.smssign.service.SmsSignService;
import com.wanmi.sbc.message.smssign.model.root.SmsSign;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>短信签名查询服务接口实现</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:49:24
 */
@RestController
@Validated
public class SmsSignQueryController implements SmsSignQueryProvider {
	@Autowired
	private SmsSignService smsSignService;

    @Autowired
    private SmsTemplateService smsTemplateService;

    @Autowired
    private SmsSendService smsSendService;

	@Override
	public BaseResponse<SmsSignPageResponse> page(@RequestBody @Valid SmsSignPageRequest smsSignPageReq) {
		SmsSignQueryRequest queryReq = new SmsSignQueryRequest();
		KsBeanUtil.copyPropertiesThird(smsSignPageReq, queryReq);
		Page<SmsSign> smsSignPage = smsSignService.page(queryReq);
		Page<SmsSignVO> newPage = smsSignPage.map(entity -> smsSignService.wrapperVo(entity));
		MicroServicePage<SmsSignVO> microPage = new MicroServicePage<>(newPage, smsSignPageReq.getPageable());
		SmsSignPageResponse finalRes = new SmsSignPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<SmsSignListResponse> list(@RequestBody @Valid SmsSignListRequest smsSignListReq) {
		SmsSignQueryRequest queryReq = new SmsSignQueryRequest();
		KsBeanUtil.copyPropertiesThird(smsSignListReq, queryReq);
		List<SmsSign> smsSignList = smsSignService.list(queryReq);
		List<SmsSignVO> newList = smsSignList.stream().map(entity -> smsSignService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new SmsSignListResponse(newList));
	}

	@Override
	public BaseResponse<SmsSignByIdResponse> getById(@RequestBody @Valid SmsSignByIdRequest smsSignByIdRequest) {
		SmsSign smsSign = smsSignService.getById(smsSignByIdRequest.getId());
		return BaseResponse.success(new SmsSignByIdResponse(smsSignService.wrapperVo(smsSign)));
	}

    @Override
	public BaseResponse check(@RequestBody @Valid SmsSignCheckByIdRequest request){
        //验证未删除模板、未结束的任务是否存在使用
        if (smsTemplateService.count(SmsTemplateQueryRequest.builder().signId(request.getId())
                .delFlag(DeleteFlag.NO).build()) > 0
                || smsSendService.count(SmsSendQueryRequest.builder().signId(request.getId())
                .noStatus(SendStatus.END).build())> 0) {
            throw new SbcRuntimeException("K-300204");
        }
        return BaseResponse.SUCCESSFUL();
    }

    @Override
	public BaseResponse<SmsSignListResponse> getBySmsSignNameAndAndDelFlag(@RequestBody @Valid SmsSignQueryRequest request) {
		List<SmsSign> smsSignList = smsSignService.getBySmsSignNameAndAndDelFlag(request);
		List<SmsSignVO> newList = smsSignList.stream().map(entity -> smsSignService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new SmsSignListResponse(newList));
	}
}

