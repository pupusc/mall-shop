package com.wanmi.sbc.message.provider.impl.smsbase;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.SmsProxy;
import com.wanmi.sbc.message.api.provider.smsbase.SmsBaseProvider;
import com.wanmi.sbc.message.api.request.smsbase.SmsSendRequest;
import com.wanmi.sbc.message.smssenddetail.model.root.SmsSendDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * <p>短信基本接口实现</p>
 * @author dyt
 * @date 2019-12-03 15:36:05
 */
@RestController
@Validated
public class SmsBaseController implements SmsBaseProvider {

    @Autowired
	private SmsProxy smsProxy;

	@Override
    public BaseResponse send(@RequestBody @Valid SmsSendRequest request) {
        SmsSendDetail smsSend = new SmsSendDetail();
		KsBeanUtil.copyPropertiesThird(request, smsSend);
		if(Objects.nonNull(request.getTemplateParamDTO())) {
            smsSend.setTemplateParam(JSON.toJSONString(request.getTemplateParamDTO()));
        }
        smsProxy.sendSms(smsSend);
		return BaseResponse.SUCCESSFUL();
	}
}

