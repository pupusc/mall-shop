package com.wanmi.sbc.marketing.api.provider.grouponsetting;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.grouponsetting.GrouponSettingPageRequest;
import com.wanmi.sbc.marketing.api.response.grouponsetting.GrouponSettingAuditFlagResponse;
import com.wanmi.sbc.marketing.api.response.grouponsetting.GrouponSettingPageResponse;
import com.wanmi.sbc.marketing.api.request.grouponsetting.GrouponSettingListRequest;
import com.wanmi.sbc.marketing.api.response.grouponsetting.GrouponSettingListResponse;
import com.wanmi.sbc.marketing.api.request.grouponsetting.GrouponSettingByIdRequest;
import com.wanmi.sbc.marketing.api.response.grouponsetting.GrouponSettingByIdResponse;
import com.wanmi.sbc.marketing.api.request.grouponsetting.GrouponAuditFlagSaveRequest;
import com.wanmi.sbc.marketing.api.request.grouponsetting.GrouponPosterSaveRequest;
import com.wanmi.sbc.marketing.api.request.grouponsetting.GrouponRuleSaveRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>拼团活动设置表查询服务Provider</p>
 * @author groupon
 * @date 2019-05-15 14:19:49
 */
@FeignClient(value = "${application.marketing.name}", contextId = "GrouponSettingQueryProvider")
public interface GrouponSettingQueryProvider {

	/**
	 * 分页查询拼团活动设置表API
	 *
	 * @author groupon
	 * @return 拼团活动设置表分页列表信息 {@link GrouponSettingPageResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/grouponsetting")
	BaseResponse<GrouponSettingPageResponse> getSetting();

	/**
	 * 列表查询拼团活动设置表API
	 *
	 * @author groupon
	 * @param grouponSettingListReq 列表请求参数和筛选对象 {@link GrouponSettingListRequest}
	 * @return 拼团活动设置表的列表信息 {@link GrouponSettingListResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/grouponsetting/list")
	BaseResponse<GrouponSettingListResponse> list(@RequestBody @Valid GrouponSettingListRequest grouponSettingListReq);

	/**
	 * 单个查询拼团活动设置表API
	 *
	 * @author groupon
	 * @param grouponSettingByIdRequest 单个查询拼团活动设置表请求参数 {@link GrouponSettingByIdRequest}
	 * @return 拼团活动设置表详情 {@link GrouponSettingByIdResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/grouponsetting/get-by-id")
	BaseResponse<GrouponSettingByIdResponse> getById(@RequestBody @Valid GrouponSettingByIdRequest
                                                             grouponSettingByIdRequest);

	/**
	 * 获取拼团商品审核状态
	 * @return
	 */
	@PostMapping("/marketing/${application.marketing.version}/grouponsetting/get-goods-audit-flag")
	BaseResponse<GrouponSettingAuditFlagResponse> getGoodsAuditFlag();
}

