package com.wanmi.sbc.marketing.provider.impl.grouponsetting;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.marketing.api.response.grouponsetting.GrouponSettingAuditFlagResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.grouponsetting.GrouponSettingQueryProvider;
import com.wanmi.sbc.marketing.api.request.grouponsetting.GrouponSettingQueryRequest;
import com.wanmi.sbc.marketing.api.response.grouponsetting.GrouponSettingPageResponse;
import com.wanmi.sbc.marketing.api.request.grouponsetting.GrouponSettingListRequest;
import com.wanmi.sbc.marketing.api.response.grouponsetting.GrouponSettingListResponse;
import com.wanmi.sbc.marketing.api.request.grouponsetting.GrouponSettingByIdRequest;
import com.wanmi.sbc.marketing.api.response.grouponsetting.GrouponSettingByIdResponse;
import com.wanmi.sbc.marketing.bean.vo.GrouponSettingVO;
import com.wanmi.sbc.marketing.grouponsetting.service.GrouponSettingService;
import com.wanmi.sbc.marketing.grouponsetting.model.root.GrouponSetting;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>拼团活动信息表查询服务接口实现</p>
 * @author groupon
 * @date 2019-05-15 14:19:49
 */
@RestController
@Validated
public class GrouponSettingQueryController implements GrouponSettingQueryProvider {
	@Autowired
	private GrouponSettingService grouponSettingService;

	@Override
	public BaseResponse<GrouponSettingPageResponse> getSetting() {
		GrouponSettingPageResponse grouponSettingPageResponse = new GrouponSettingPageResponse();
		List<GrouponSetting> grouponSettings = grouponSettingService.getSetting();
		if (CollectionUtils.isEmpty(grouponSettings)){
			return BaseResponse.success(grouponSettingPageResponse);
		}
		GrouponSetting grouponSetting = grouponSettings.get(0);
		GrouponSettingVO grouponSettingVO = new GrouponSettingVO();
		grouponSettingPageResponse.setGrouponSettingVO(grouponSettingVO);
		grouponSettingPageResponse.getGrouponSettingVO().setId(grouponSetting.getId());
		grouponSettingPageResponse.getGrouponSettingVO().setGoodsAuditFlag(grouponSetting.getGoodsAuditFlag());
		grouponSettingPageResponse.getGrouponSettingVO().setAdvert(grouponSetting.getAdvert());
		grouponSettingPageResponse.getGrouponSettingVO().setRule(grouponSetting.getRule());
		return BaseResponse.success(grouponSettingPageResponse);
	}

	@Override
	public BaseResponse<GrouponSettingListResponse> list(@RequestBody @Valid GrouponSettingListRequest grouponSettingListReq) {
		GrouponSettingQueryRequest queryReq = new GrouponSettingQueryRequest();
		KsBeanUtil.copyPropertiesThird(grouponSettingListReq, queryReq);
		List<GrouponSetting> grouponSettingList = grouponSettingService.list(queryReq);
		List<GrouponSettingVO> newList = grouponSettingList.stream().map(entity -> grouponSettingService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new GrouponSettingListResponse(newList));
	}

	@Override
	public BaseResponse<GrouponSettingByIdResponse> getById(@RequestBody @Valid GrouponSettingByIdRequest grouponSettingByIdRequest) {
		GrouponSetting grouponSetting = grouponSettingService.getById(grouponSettingByIdRequest.getId());
		return BaseResponse.success(new GrouponSettingByIdResponse(grouponSettingService.wrapperVo(grouponSetting)));
	}

	@Override
	public BaseResponse<GrouponSettingAuditFlagResponse> getGoodsAuditFlag() {
		DefaultFlag goodsAuditFlag = grouponSettingService.getGoodsAuditFlag();
		return BaseResponse.success(new GrouponSettingAuditFlagResponse(goodsAuditFlag));
	}

}

