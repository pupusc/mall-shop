package com.wanmi.sbc.goods.api.provider.ruleinfo;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoAddRequest;
import com.wanmi.sbc.goods.api.response.ruleinfo.RuleInfoAddResponse;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoModifyRequest;
import com.wanmi.sbc.goods.api.response.ruleinfo.RuleInfoModifyResponse;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoDelByIdRequest;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>规则说明保存服务Provider</p>
 * @author zxd
 * @date 2020-05-25 18:55:56
 */
@FeignClient(value = "${application.goods.name}", contextId = "RuleInfoProvider")
public interface RuleInfoProvider {

	/**
	 * 新增规则说明API
	 *
	 * @author zxd
	 * @param ruleInfoAddRequest 规则说明新增参数结构 {@link RuleInfoAddRequest}
	 * @return 新增的规则说明信息 {@link RuleInfoAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/ruleinfo/add")
	BaseResponse<RuleInfoAddResponse> add(@RequestBody @Valid RuleInfoAddRequest ruleInfoAddRequest);

	/**
	 * 修改规则说明API
	 *
	 * @author zxd
	 * @param ruleInfoModifyRequest 规则说明修改参数结构 {@link RuleInfoModifyRequest}
	 * @return 修改的规则说明信息 {@link RuleInfoModifyResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/ruleinfo/modify")
	BaseResponse<RuleInfoModifyResponse> modify(@RequestBody @Valid RuleInfoModifyRequest ruleInfoModifyRequest);

	/**
	 * 单个删除规则说明API
	 *
	 * @author zxd
	 * @param ruleInfoDelByIdRequest 单个删除参数结构 {@link RuleInfoDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/ruleinfo/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid RuleInfoDelByIdRequest ruleInfoDelByIdRequest);

	/**
	 * 批量删除规则说明API
	 *
	 * @author zxd
	 * @param ruleInfoDelByIdListRequest 批量删除参数结构 {@link RuleInfoDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/ruleinfo/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid RuleInfoDelByIdListRequest ruleInfoDelByIdListRequest);

}

