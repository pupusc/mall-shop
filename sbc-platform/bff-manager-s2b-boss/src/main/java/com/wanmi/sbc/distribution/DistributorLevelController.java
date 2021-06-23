package com.wanmi.sbc.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.distribution.DistributorLevelProvider;
import com.wanmi.sbc.customer.api.provider.distribution.DistributorLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.distribution.DistributorLevelDeleteRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributorLevelBaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 分销员等级
 * @author: Geek Wang
 * @createDate: 2019/6/13 17:16
 * @version: 1.0
 */
@Api(description = "分销员等级服务" ,tags ="DistributorLevelController")
@RestController
@RequestMapping("/distributor-level")
@Validated
public class DistributorLevelController {

	@Autowired
	private DistributorLevelQueryProvider distributorLevelQueryProvider;

	@Autowired
	private DistributorLevelProvider distributorLevelProvider;

	/**
	 * 查询分销员等级基础信息列表（仅包含字段：分销员等级ID、分销员等级名称）
	 * @return
	 */
	@ApiOperation(value = "查询分销员等级基础信息列表（仅包含字段：分销员等级ID、分销员等级名称）")
	@RequestMapping(value = "/list-base-info",method = RequestMethod.POST)
	public BaseResponse<DistributorLevelBaseResponse> listBaseInfo(){
		return distributorLevelQueryProvider.listBaseInfo();
	}

	/**
	 * 删除分销员等级
	 */
	@ApiOperation(value = "删除分销员等级")
	@RequestMapping(method = RequestMethod.DELETE)
	public BaseResponse delete(@RequestBody @Valid DistributorLevelDeleteRequest request) {
		return distributorLevelProvider.delete(request);
	}
}
