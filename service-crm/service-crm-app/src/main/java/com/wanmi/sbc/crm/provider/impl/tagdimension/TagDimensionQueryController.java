package com.wanmi.sbc.crm.provider.impl.tagdimension;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.tagdimension.TagDimensionQueryProvider;
import com.wanmi.sbc.crm.api.request.tagdimension.*;
import com.wanmi.sbc.crm.api.response.tagdimension.TagDimensionBigJsonResponse;
import com.wanmi.sbc.crm.api.response.tagdimension.TagDimensionByIdResponse;
import com.wanmi.sbc.crm.api.response.tagdimension.TagDimensionListResponse;
import com.wanmi.sbc.crm.api.response.tagdimension.TagDimensionPageResponse;
import com.wanmi.sbc.crm.bean.enums.TagDimensionFirstLastType;
import com.wanmi.sbc.crm.bean.enums.TagType;
import com.wanmi.sbc.crm.bean.vo.TagDimensionVO;
import com.wanmi.sbc.crm.tagdimension.model.root.TagDimension;
import com.wanmi.sbc.crm.tagdimension.service.TagDimensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>标签维度查询服务接口实现</p>
 * @author dyt
 * @date 2020-03-12 16:00:30
 */
@RestController
@Validated
public class TagDimensionQueryController implements TagDimensionQueryProvider {
	@Autowired
	private TagDimensionService tagDimensionService;



	@Override
	public BaseResponse<TagDimensionPageResponse> page(@RequestBody @Valid TagDimensionPageRequest tagDimensionPageReq) {
		TagDimensionQueryRequest queryReq = KsBeanUtil.convert(tagDimensionPageReq, TagDimensionQueryRequest.class);
		Page<TagDimension> tagDimensionPage = tagDimensionService.page(queryReq);
		Page<TagDimensionVO> newPage = tagDimensionPage.map(entity -> tagDimensionService.wrapperVo(entity));
		MicroServicePage<TagDimensionVO> microPage = new MicroServicePage<>(newPage, tagDimensionPageReq.getPageable());
		TagDimensionPageResponse finalRes = new TagDimensionPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<TagDimensionListResponse> list(@RequestBody @Valid TagDimensionListRequest tagDimensionListReq) {
		TagDimensionQueryRequest queryReq = KsBeanUtil.convert(tagDimensionListReq, TagDimensionQueryRequest.class);
		if(TagType.PREFERENCE.equals(tagDimensionListReq.getTagType())){
			queryReq.setFirstLastType(TagDimensionFirstLastType.NO_FIRST_LAST);
		}
		List<TagDimension> tagDimensionList = tagDimensionService.list(queryReq);
		List<TagDimensionVO> newList = tagDimensionList.stream().map(entity -> tagDimensionService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new TagDimensionListResponse(newList));
	}

	@Override
	public BaseResponse<TagDimensionByIdResponse> getById(@RequestBody @Valid TagDimensionByIdRequest tagDimensionByIdRequest) {
		TagDimension tagDimension =
		tagDimensionService.getOne(tagDimensionByIdRequest.getId());
		return BaseResponse.success(new TagDimensionByIdResponse(tagDimensionService.wrapperVo(tagDimension)));
	}


	@Override
	public BaseResponse<TagDimensionBigJsonResponse> getBigJson(@RequestBody @Valid TagDimensionBigJsonRequest request) {
		List<TagDimensionVO> otherList =tagDimensionService.selectOtherTagList(request);
		return BaseResponse.success(TagDimensionBigJsonResponse.builder()
				.otherList(otherList).build());
	}

	@Override
	public BaseResponse<TagDimensionBigJsonResponse> getPreferenceBigJson() {
		List<TagDimensionVO> preferenceList =tagDimensionService.selectPreferenceTagList();
		List<TagDimensionVO> preferenceParamList =tagDimensionService.selectPreferenceParamList();
		return BaseResponse.success(TagDimensionBigJsonResponse.builder().preferenceList(preferenceList)
				.preferenceParamList(preferenceParamList).build());
	}
}

