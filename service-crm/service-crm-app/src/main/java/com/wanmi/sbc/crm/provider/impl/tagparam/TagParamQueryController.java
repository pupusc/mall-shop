package com.wanmi.sbc.crm.provider.impl.tagparam;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.tagparam.TagParamQueryProvider;
import com.wanmi.sbc.crm.api.request.tagparam.TagParamByIdRequest;
import com.wanmi.sbc.crm.api.request.tagparam.TagParamListRequest;
import com.wanmi.sbc.crm.api.request.tagparam.TagParamPageRequest;
import com.wanmi.sbc.crm.api.request.tagparam.TagParamQueryRequest;
import com.wanmi.sbc.crm.api.response.tagparam.TagParamByIdResponse;
import com.wanmi.sbc.crm.api.response.tagparam.TagParamListResponse;
import com.wanmi.sbc.crm.api.response.tagparam.TagParamPageResponse;
import com.wanmi.sbc.crm.bean.enums.TagDimensionFirstLastType;
import com.wanmi.sbc.crm.bean.enums.TagType;
import com.wanmi.sbc.crm.bean.vo.TagParamVO;
import com.wanmi.sbc.crm.tagparam.model.root.TagParam;
import com.wanmi.sbc.crm.tagparam.service.TagParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>标签参数查询服务接口实现</p>
 * @author dyt
 * @date 2020-03-12 15:59:49
 */
@RestController
@Validated
public class TagParamQueryController implements TagParamQueryProvider {
	@Autowired
	private TagParamService tagParamService;

	@Override
	public BaseResponse<TagParamPageResponse> page(@RequestBody @Valid TagParamPageRequest tagParamPageReq) {
		TagParamQueryRequest queryReq = KsBeanUtil.convert(tagParamPageReq, TagParamQueryRequest.class);
		Page<TagParam> tagParamPage = tagParamService.page(queryReq);
		Page<TagParamVO> newPage = tagParamPage.map(entity -> tagParamService.wrapperVo(entity));
		MicroServicePage<TagParamVO> microPage = new MicroServicePage<>(newPage, tagParamPageReq.getPageable());
		TagParamPageResponse finalRes = new TagParamPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<TagParamListResponse> list(@RequestBody @Valid TagParamListRequest tagParamListReq) {
		TagParamQueryRequest queryReq = null; //KsBeanUtil.convert(tagParamListReq, TagParamQueryRequest.class);
		//偏好类标签
		if(TagType.PREFERENCE.equals(tagParamListReq.getTagType())){
			queryReq = TagParamQueryRequest.builder().tagDimensionId(tagParamListReq.getTagDimensionId()).type(TagType.PREFERENCE.toValue()).build();
		}else{
			//其他标签
			Integer tageType = tagParamListReq.getFirstLastType().equals(TagDimensionFirstLastType.NO_FIRST_LAST)?1:0;
			List<Long> list = Arrays.asList(1L,2L);
			queryReq = TagParamQueryRequest.builder().tagDimensionId(tagParamListReq.getTagDimensionId()).tagType(tageType).typeList(list).build();
		}
		List<TagParam> tagParamList = tagParamService.list(queryReq);
		List<TagParamVO> newList = tagParamList.stream().map(entity -> tagParamService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new TagParamListResponse(newList));
	}

	@Override
	public BaseResponse<TagParamByIdResponse> getById(@RequestBody @Valid TagParamByIdRequest tagParamByIdRequest) {
		TagParam tagParam =
		tagParamService.getOne(tagParamByIdRequest.getId());
		return BaseResponse.success(new TagParamByIdResponse(tagParamService.wrapperVo(tagParam)));
	}

}

