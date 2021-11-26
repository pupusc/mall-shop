package com.wanmi.sbc.crm.provider.impl.tagdimension;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.tagdimension.TagDimensionProvider;
import com.wanmi.sbc.crm.api.request.tagdimension.TagDimensionAddRequest;
import com.wanmi.sbc.crm.api.response.tagdimension.TagDimensionAddResponse;
import com.wanmi.sbc.crm.api.request.tagdimension.TagDimensionModifyRequest;
import com.wanmi.sbc.crm.api.response.tagdimension.TagDimensionModifyResponse;
import com.wanmi.sbc.crm.api.request.tagdimension.TagDimensionDelByIdRequest;
import com.wanmi.sbc.crm.api.request.tagdimension.TagDimensionDelByIdListRequest;
import com.wanmi.sbc.crm.tagdimension.service.TagDimensionService;
import com.wanmi.sbc.crm.tagdimension.model.root.TagDimension;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

/**
 * <p>标签维度保存服务接口实现</p>
 * @author dyt
 * @date 2020-03-12 16:00:30
 */
@RestController
@Validated
public class TagDimensionController implements TagDimensionProvider {
	@Autowired
	private TagDimensionService tagDimensionService;

	@Override
	public BaseResponse<TagDimensionAddResponse> add(@RequestBody @Valid TagDimensionAddRequest tagDimensionAddRequest) {
		TagDimension tagDimension = KsBeanUtil.convert(tagDimensionAddRequest, TagDimension.class);
		return BaseResponse.success(new TagDimensionAddResponse(
				tagDimensionService.wrapperVo(tagDimensionService.add(tagDimension))));
	}

	@Override
	public BaseResponse<TagDimensionModifyResponse> modify(@RequestBody @Valid TagDimensionModifyRequest tagDimensionModifyRequest) {
		TagDimension tagDimension = KsBeanUtil.convert(tagDimensionModifyRequest, TagDimension.class);
		return BaseResponse.success(new TagDimensionModifyResponse(
				tagDimensionService.wrapperVo(tagDimensionService.modify(tagDimension))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid TagDimensionDelByIdRequest tagDimensionDelByIdRequest) {
		tagDimensionService.deleteById(tagDimensionDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid TagDimensionDelByIdListRequest tagDimensionDelByIdListRequest) {
		tagDimensionService.deleteByIdList(tagDimensionDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

