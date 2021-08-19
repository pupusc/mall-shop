package com.wanmi.sbc.crm.provider.impl.tagparam;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.tagparam.TagParamProvider;
import com.wanmi.sbc.crm.api.request.tagparam.TagParamAddRequest;
import com.wanmi.sbc.crm.api.response.tagparam.TagParamAddResponse;
import com.wanmi.sbc.crm.api.request.tagparam.TagParamModifyRequest;
import com.wanmi.sbc.crm.api.response.tagparam.TagParamModifyResponse;
import com.wanmi.sbc.crm.api.request.tagparam.TagParamDelByIdRequest;
import com.wanmi.sbc.crm.api.request.tagparam.TagParamDelByIdListRequest;
import com.wanmi.sbc.crm.tagparam.service.TagParamService;
import com.wanmi.sbc.crm.tagparam.model.root.TagParam;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

/**
 * <p>标签参数保存服务接口实现</p>
 * @author dyt
 * @date 2020-03-12 15:59:49
 */
@RestController
@Validated
public class TagParamController implements TagParamProvider {
	@Autowired
	private TagParamService tagParamService;

	@Override
	public BaseResponse<TagParamAddResponse> add(@RequestBody @Valid TagParamAddRequest tagParamAddRequest) {
		TagParam tagParam = KsBeanUtil.convert(tagParamAddRequest, TagParam.class);
		return BaseResponse.success(new TagParamAddResponse(
				tagParamService.wrapperVo(tagParamService.add(tagParam))));
	}

	@Override
	public BaseResponse<TagParamModifyResponse> modify(@RequestBody @Valid TagParamModifyRequest tagParamModifyRequest) {
		TagParam tagParam = KsBeanUtil.convert(tagParamModifyRequest, TagParam.class);
		return BaseResponse.success(new TagParamModifyResponse(
				tagParamService.wrapperVo(tagParamService.modify(tagParam))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid TagParamDelByIdRequest tagParamDelByIdRequest) {
		tagParamService.deleteById(tagParamDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid TagParamDelByIdListRequest tagParamDelByIdListRequest) {
		tagParamService.deleteByIdList(tagParamDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

