package com.wanmi.sbc.setting.api.provider.syssms;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.syssms.SysSmsPageRequest;
import com.wanmi.sbc.setting.api.response.syssms.SmsSupplierRopResponse;
import com.wanmi.sbc.setting.api.response.syssms.SysSmsPageResponse;
import com.wanmi.sbc.setting.api.request.syssms.SysSmsListRequest;
import com.wanmi.sbc.setting.api.response.syssms.SysSmsListResponse;
import com.wanmi.sbc.setting.api.request.syssms.SysSmsByIdRequest;
import com.wanmi.sbc.setting.api.response.syssms.SysSmsByIdResponse;
import com.wanmi.sbc.setting.bean.vo.SysSmsVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>系统短信配置查询服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:13:47
 */
@FeignClient(value = "${application.setting.name}", contextId = "SysSmsQueryProvider")
public interface SysSmsQueryProvider {

	/**
	 * 分页查询系统短信配置API
	 *
	 * @author lq
	 * @param sysSmsPageReq 分页请求参数和筛选对象 {@link SysSmsPageRequest}
	 * @return 系统短信配置分页列表信息 {@link SysSmsPageResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/syssms/page")
	 BaseResponse <SysSmsPageResponse> page(@RequestBody @Valid SysSmsPageRequest sysSmsPageReq);

	/**
	 * 列表查询系统短信配置API
	 *
	 * @author lq
	 * @return 系统短信配置的列表信息 {@link SysSmsListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/syssms/list")
	 BaseResponse<SysSmsListResponse> list();

	/**
	 * 单个查询系统短信配置API
	 *
	 * @author lq
	 * @param sysSmsByIdRequest 单个查询系统短信配置请求参数 {@link SysSmsByIdRequest}
	 * @return 系统短信配置详情 {@link SysSmsByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/syssms/get-by-id")
	BaseResponse<SmsSupplierRopResponse> getById(@RequestBody @Valid SysSmsByIdRequest sysSmsByIdRequest);
}

