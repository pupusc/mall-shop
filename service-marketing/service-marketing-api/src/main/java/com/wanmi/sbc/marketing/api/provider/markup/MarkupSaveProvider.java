package com.wanmi.sbc.marketing.api.provider.markup;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.markup.MarkupAddRequest;
import com.wanmi.sbc.marketing.api.response.markup.MarkupAddResponse;
import com.wanmi.sbc.marketing.api.request.markup.MarkupModifyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>加价购活动保存服务Provider</p>
 * @author he
 * @date 2021-02-04 16:09:09
 */
@FeignClient(value = "${application.marketing.name}", contextId = "MarkupSaveProvider")
public interface MarkupSaveProvider {

	/**
	 * 新增加价购活动API
	 *
	 * @author he
	 * @param markupAddRequest 加价购活动新增参数结构 {@link MarkupAddRequest}
	 * @return 新增的加价购活动信息 {@link MarkupAddResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/markup/add")
	BaseResponse<MarkupAddResponse> add(@RequestBody @Valid MarkupAddRequest markupAddRequest);

	/**
	 * 修改加价购活动API
	 *
	 * @author he
	 * @param markupModifyRequest 加价购活动修改参数结构 {@link MarkupModifyRequest}
	 * @return 修改的加价购活动信息 {@link MarkupModifyRequest}
	 */
	@PostMapping("/marketing/${application.marketing.version}/markup/modify")
	BaseResponse modify(@RequestBody @Valid MarkupModifyRequest markupModifyRequest);


}

