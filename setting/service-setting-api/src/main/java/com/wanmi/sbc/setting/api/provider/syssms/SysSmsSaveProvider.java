package com.wanmi.sbc.setting.api.provider.syssms;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.syssms.SysSmsAddRequest;
import com.wanmi.sbc.setting.api.response.syssms.SmsSupplierRopResponse;
import com.wanmi.sbc.setting.api.response.syssms.SysSmsAddResponse;
import com.wanmi.sbc.setting.api.request.syssms.SysSmsModifyRequest;
import com.wanmi.sbc.setting.api.response.syssms.SysSmsModifyResponse;
import com.wanmi.sbc.setting.api.request.syssms.SysSmsDelByIdRequest;
import com.wanmi.sbc.setting.api.request.syssms.SysSmsDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>系统短信配置保存服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:13:47
 */
@FeignClient(value = "${application.setting.name}", contextId = "SysSmsSaveProvider")
public interface SysSmsSaveProvider {

	/**
	 * 新增系统短信配置API
	 *
	 * @author lq
	 * @param sysSmsAddRequest 系统短信配置新增参数结构 {@link SysSmsAddRequest}
	 * @return 新增的系统短信配置信息 {@link SysSmsAddResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/syssms/add")
	BaseResponse<SmsSupplierRopResponse> add(@RequestBody @Valid SysSmsAddRequest sysSmsAddRequest);

	/**
	 * 修改系统短信配置API
	 *
	 * @author lq
	 * @param sysSmsModifyRequest 系统短信配置修改参数结构 {@link SysSmsModifyRequest}
	 * @return 修改的系统短信配置信息 {@link SysSmsModifyResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/syssms/modify")
	BaseResponse<SmsSupplierRopResponse> modify(@RequestBody @Valid SysSmsModifyRequest sysSmsModifyRequest);

	/**
	 * 单个删除系统短信配置API
	 *
	 * @author lq
	 * @param sysSmsDelByIdRequest 单个删除参数结构 {@link SysSmsDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/syssms/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid SysSmsDelByIdRequest sysSmsDelByIdRequest);

	/**
	 * 批量删除系统短信配置API
	 *
	 * @author lq
	 * @param sysSmsDelByIdListRequest 批量删除参数结构 {@link SysSmsDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/syssms/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid SysSmsDelByIdListRequest sysSmsDelByIdListRequest);

}

