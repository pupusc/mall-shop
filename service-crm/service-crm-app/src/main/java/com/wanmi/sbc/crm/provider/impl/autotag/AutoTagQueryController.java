package com.wanmi.sbc.crm.provider.impl.autotag;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.autotag.AutoTagQueryProvider;
import com.wanmi.sbc.crm.api.request.autotag.AutoTagByIdRequest;
import com.wanmi.sbc.crm.api.request.autotag.AutoTagListRequest;
import com.wanmi.sbc.crm.api.request.autotag.AutoTagPageRequest;
import com.wanmi.sbc.crm.api.request.autotag.AutoTagQueryRequest;
import com.wanmi.sbc.crm.api.response.autotag.*;
import com.wanmi.sbc.crm.autotag.model.root.AutoTag;
import com.wanmi.sbc.crm.autotag.service.AutoTagService;
import com.wanmi.sbc.crm.bean.vo.AutoTagSelectVO;
import com.wanmi.sbc.crm.bean.vo.AutoTagVO;
import com.wanmi.sbc.crm.bean.vo.PreferenceTagListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>自动标签查询服务接口实现</p>
 * @author dyt
 * @date 2020-03-11 14:47:32
 */
@RestController
@Validated
public class AutoTagQueryController implements AutoTagQueryProvider {
	@Autowired
	private AutoTagService autoTagService;

	@Override
	public BaseResponse<AutoTagPageResponse> page(@RequestBody @Valid AutoTagPageRequest autoTagPageReq) {
		AutoTagQueryRequest queryReq = KsBeanUtil.convert(autoTagPageReq, AutoTagQueryRequest.class);
		Page<AutoTag> autoTagPage = autoTagService.page(queryReq);
		Page<AutoTagVO> newPage = autoTagPage.map(entity -> autoTagService.wrapperVo(entity));
		MicroServicePage<AutoTagVO> microPage = new MicroServicePage<>(newPage, autoTagPageReq.getPageable());
		AutoTagPageResponse finalRes = new AutoTagPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<PreferenceTagListResponse> getPreferenceList(@Valid AutoTagListRequest autoTagListReq) {
		AutoTagQueryRequest queryReq = KsBeanUtil.convert(autoTagListReq, AutoTagQueryRequest.class);
		List<AutoTag> autoTagList = autoTagService.list(queryReq);
		List<PreferenceTagListVo> newList = autoTagList.stream().map(entity -> autoTagService.wrapperPreferenceTagListVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new PreferenceTagListResponse(newList));
	}

	@Override
	public BaseResponse<AutoTagListResponse> list(@RequestBody @Valid AutoTagListRequest autoTagListReq) {
		AutoTagQueryRequest queryReq = KsBeanUtil.convert(autoTagListReq, AutoTagQueryRequest.class);
		List<AutoTag> autoTagList = autoTagService.list(queryReq);
		List<AutoTagVO> newList = autoTagList.stream().map(entity -> autoTagService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new AutoTagListResponse(newList));
	}

	@Override
	public BaseResponse<AutoTagByIdResponse> getById(@RequestBody @Valid AutoTagByIdRequest autoTagByIdRequest) {
		AutoTag autoTag = autoTagService.getOne(autoTagByIdRequest.getId());
        AutoTagVO autoTagVO = autoTagService.wrapperVo(autoTag);
        if(StringUtils.isNotBlank(autoTag.getRuleJson())) {
			AutoTagSelectVO autoTagSelectVO = JSON.parseObject(autoTag.getRuleJson(), AutoTagSelectVO.class);
			autoTagSelectVO.setId(autoTag.getId());
			autoTagSelectVO.setTagName(autoTag.getTagName());
			autoTagSelectVO.setDay(autoTag.getDay());
			autoTagSelectVO.setType(autoTag.getType());
            autoTagVO.setAutoTagSelect(autoTagSelectVO);
			autoTagVO.setDataRange(autoTagSelectVO.getDataRange());
        }
		autoTagVO.setRuleJson("");
		return BaseResponse.success(new AutoTagByIdResponse(autoTagVO));
	}

	@Override
    public BaseResponse<AutoTagInitListResponse> systemList() {
        return BaseResponse.success(new AutoTagInitListResponse(autoTagService.systemList()));
    }

	/**
	 * 非系统标签总数API
	 *
	 * @return 系统标签的列表信息 {@link AutoTagInitListResponse}
	 * @author dyt
	 */
	@Override
	public BaseResponse<Long> getCount() {
		return BaseResponse.success(autoTagService.getCount());
	}
}

