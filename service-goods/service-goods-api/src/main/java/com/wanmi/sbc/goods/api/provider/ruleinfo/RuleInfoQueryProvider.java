package com.wanmi.sbc.goods.api.provider.ruleinfo;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoPageRequest;
import com.wanmi.sbc.goods.api.response.ruleinfo.RuleInfoPageResponse;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoListRequest;
import com.wanmi.sbc.goods.api.response.ruleinfo.RuleInfoListResponse;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoByIdRequest;
import com.wanmi.sbc.goods.api.response.ruleinfo.RuleInfoByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>规则说明查询服务Provider</p>
 * @author zxd
 * @date 2020-05-25 18:55:56
 */
@FeignClient(value = "${application.goods.name}", contextId = "RuleInfoQueryProvider")
public interface RuleInfoQueryProvider {

	/**
	 * 分页查询规则说明API
	 *
	 * @author zxd
	 * @param ruleInfoPageReq 分页请求参数和筛选对象 {@link RuleInfoPageRequest}
	 * @return 规则说明分页列表信息 {@link RuleInfoPageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/ruleinfo/page")
	BaseResponse<RuleInfoPageResponse> page(@RequestBody @Valid RuleInfoPageRequest ruleInfoPageReq);

	/**
	 * 列表查询规则说明API
	 *
	 * @author zxd
	 * @param ruleInfoListReq 列表请求参数和筛选对象 {@link RuleInfoListRequest}
	 * @return 规则说明的列表信息 {@link RuleInfoListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/ruleinfo/list")
	BaseResponse<RuleInfoListResponse> list(@RequestBody @Valid RuleInfoListRequest ruleInfoListReq);

	/**
	 * 单个查询规则说明API
	 *
	 * @author zxd
	 * @param ruleInfoByIdRequest 单个查询规则说明请求参数 {@link RuleInfoByIdRequest}
	 * @return 规则说明详情 {@link RuleInfoByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/ruleinfo/get-by-id")
	BaseResponse<RuleInfoByIdResponse> getById(@RequestBody @Valid RuleInfoByIdRequest ruleInfoByIdRequest);

}

