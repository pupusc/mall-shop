package com.wanmi.sbc.marketing.provider.impl.markup;

import com.wanmi.sbc.marketing.bean.vo.MarketingVO;
import com.wanmi.sbc.marketing.markup.request.MarkupAllSaveRequest;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.markup.MarkupSaveProvider;
import com.wanmi.sbc.marketing.api.request.markup.MarkupAddRequest;
import com.wanmi.sbc.marketing.api.response.markup.MarkupAddResponse;
import com.wanmi.sbc.marketing.api.request.markup.MarkupModifyRequest;
import com.wanmi.sbc.marketing.markup.service.MarkupService;

import javax.validation.Valid;

/**
 * <p>加价购活动保存服务接口实现</p>
 * @author he
 * @date 2021-02-04 16:09:09
 */
@RestController
@Validated
public class MarkupSaveController implements MarkupSaveProvider {
	@Autowired
	private MarkupService markupService;

	@Override
	public BaseResponse<MarkupAddResponse> add(@RequestBody @Valid MarkupAddRequest markupAddRequest) {
		MarkupAllSaveRequest markupAllSaveRequest = KsBeanUtil.convert(markupAddRequest, MarkupAllSaveRequest.class);

		return BaseResponse.success(new MarkupAddResponse(
				KsBeanUtil.convert(markupService.add(markupAllSaveRequest), MarketingVO.class)));
	}

	@Override
	public BaseResponse modify(@RequestBody @Valid MarkupModifyRequest markupModifyRequest) {

		MarkupAllSaveRequest markupSaveRequest = KsBeanUtil.convert(markupModifyRequest, MarkupAllSaveRequest.class);
		markupService.modify(markupSaveRequest);
		return  BaseResponse.SUCCESSFUL();
	}


}

