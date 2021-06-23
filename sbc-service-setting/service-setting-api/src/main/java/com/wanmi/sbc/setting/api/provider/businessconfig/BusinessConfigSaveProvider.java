package com.wanmi.sbc.setting.api.provider.businessconfig;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.businessconfig.BusinessConfigAddRequest;
import com.wanmi.sbc.setting.api.response.businessconfig.BusinessConfigAddResponse;
import com.wanmi.sbc.setting.api.request.businessconfig.BusinessConfigModifyRequest;
import com.wanmi.sbc.setting.api.response.businessconfig.BusinessConfigModifyResponse;
import com.wanmi.sbc.setting.api.request.businessconfig.BusinessConfigDelByIdRequest;
import com.wanmi.sbc.setting.api.request.businessconfig.BusinessConfigDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>招商页设置保存服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:09:10
 */
@FeignClient(value = "${application.setting.name}", contextId = "BusinessConfigSaveProvider")
public interface BusinessConfigSaveProvider {

	/**
	 * 新增招商页设置API
	 *
	 * @author lq
	 * @param businessConfigAddRequest 招商页设置新增参数结构 {@link BusinessConfigAddRequest}
	 * @return 新增的招商页设置信息 {@link BusinessConfigAddResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/businessconfig/add")
	BaseResponse<BusinessConfigAddResponse> add(@RequestBody @Valid BusinessConfigAddRequest businessConfigAddRequest);

	/**
	 * 修改招商页设置API
	 *
	 * @author lq
	 * @param businessConfigModifyRequest 招商页设置修改参数结构 {@link BusinessConfigModifyRequest}
	 * @return 修改的招商页设置信息 {@link BusinessConfigModifyResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/businessconfig/modify")
	BaseResponse<BusinessConfigModifyResponse> modify(@RequestBody @Valid BusinessConfigModifyRequest
                                                              businessConfigModifyRequest);

	/**
	 * 单个删除招商页设置API
	 *
	 * @author lq
	 * @param businessConfigDelByIdRequest 单个删除参数结构 {@link BusinessConfigDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/businessconfig/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid BusinessConfigDelByIdRequest businessConfigDelByIdRequest);

	/**
	 * 批量删除招商页设置API
	 *
	 * @author lq
	 * @param businessConfigDelByIdListRequest 批量删除参数结构 {@link BusinessConfigDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/businessconfig/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid BusinessConfigDelByIdListRequest businessConfigDelByIdListRequest);

}

