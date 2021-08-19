package com.wanmi.sbc.message.provider.impl.smssenddetail;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.smssenddetail.SmsSendDetailSaveProvider;
import com.wanmi.sbc.message.api.request.smssenddetail.*;
import com.wanmi.sbc.message.api.response.smssenddetail.SmsSendDetailAddResponse;
import com.wanmi.sbc.message.api.response.smssenddetail.SmsSendDetailModifyResponse;
import com.wanmi.sbc.message.smssenddetail.model.root.SmsSendDetail;
import com.wanmi.sbc.message.smssenddetail.service.SmsSendDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>短信发送保存服务接口实现</p>
 * @author zgl
 * @date 2019-12-03 15:43:37
 */
@RestController
@Validated
public class SmsSendDetailSaveController implements SmsSendDetailSaveProvider {
	@Autowired
	private SmsSendDetailService smsSendDetailService;

	@Override
	public BaseResponse<SmsSendDetailAddResponse> add(@RequestBody @Valid SmsSendDetailAddRequest smsSendDetailAddRequest) {
		SmsSendDetail smsSendDetail = new SmsSendDetail();
		KsBeanUtil.copyPropertiesThird(smsSendDetailAddRequest, smsSendDetail);
		return BaseResponse.success(new SmsSendDetailAddResponse(
				smsSendDetailService.wrapperVo(smsSendDetailService.add(smsSendDetail))));
	}

    @Override
	public BaseResponse batchAdd(@RequestBody @Valid SmsSendDetailBatchAddRequest request){
        smsSendDetailService.batchAdd(KsBeanUtil.convert(request.getDetailList(), SmsSendDetail.class));
        return BaseResponse.SUCCESSFUL();
    }

	@Override
	public BaseResponse<SmsSendDetailModifyResponse> modify(@RequestBody @Valid SmsSendDetailModifyRequest smsSendDetailModifyRequest) {
		SmsSendDetail smsSendDetail = new SmsSendDetail();
		KsBeanUtil.copyPropertiesThird(smsSendDetailModifyRequest, smsSendDetail);
		return BaseResponse.success(new SmsSendDetailModifyResponse(
				smsSendDetailService.wrapperVo(smsSendDetailService.modify(smsSendDetail))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid SmsSendDetailDelByIdRequest smsSendDetailDelByIdRequest) {
		smsSendDetailService.deleteById(smsSendDetailDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid SmsSendDetailDelByIdListRequest smsSendDetailDelByIdListRequest) {
		smsSendDetailService.deleteByIdList(smsSendDetailDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

