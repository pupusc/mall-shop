package com.wanmi.sbc.marketing.api.provider.marketingsuits;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.marketingsuits.*;
import com.wanmi.sbc.marketing.api.response.marketingsuits.MarketingSuitsAddResponse;
import com.wanmi.sbc.marketing.api.response.marketingsuits.MarketingSuitsModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>组合商品主表保存服务Provider</p>
 * @author zhk
 * @date 2020-04-01 20:54:00
 */
@FeignClient(value = "${application.marketing.name}", contextId = "MarketingSuitsProvider")
public interface MarketingSuitsProvider {

	/**
	 * 新增组合商品主表API
	 *
	 * @author zhk
	 * @param marketingSuitsSaveRequest 组合商品主表新增参数结构 {@link MarketingSuitsSaveRequest}
	 * @return 新增的组合商品主表信息 {@link MarketingSuitsSaveRequest}
	 */
	@PostMapping("/marketing/${application.marketing.version}/marketingsuits/add")
	BaseResponse add(@RequestBody @Valid MarketingSuitsSaveRequest marketingSuitsSaveRequest);

	/**
	 * 修改组合商品主表API
	 *
	 * @author zhk
	 * @param marketingSuitsSaveRequest 组合商品主表修改参数结构 {@link MarketingSuitsSaveRequest}
	 * @return 修改的组合商品主表信息 {@link MarketingSuitsSaveRequest}
	 */
	@PostMapping("/marketing/${application.marketing.version}/marketingsuits/modify")
	BaseResponse modify(@RequestBody @Valid MarketingSuitsSaveRequest marketingSuitsSaveRequest);

	/**
	 * 批量删除组合商品主表API
	 *
	 * @author zhk
	 * @param marketingSuitsDelByIdListRequest 批量删除参数结构 {@link MarketingSuitsDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/marketingsuits/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid MarketingSuitsDelByIdListRequest marketingSuitsDelByIdListRequest);

}

