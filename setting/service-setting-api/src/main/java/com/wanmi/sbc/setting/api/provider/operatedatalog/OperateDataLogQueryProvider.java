package com.wanmi.sbc.setting.api.provider.operatedatalog;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.operatedatalog.OperateDataLogPageRequest;
import com.wanmi.sbc.setting.api.response.operatedatalog.OperateDataLogPageResponse;
import com.wanmi.sbc.setting.api.request.operatedatalog.OperateDataLogListRequest;
import com.wanmi.sbc.setting.api.response.operatedatalog.OperateDataLogListResponse;
import com.wanmi.sbc.setting.api.request.operatedatalog.OperateDataLogByIdRequest;
import com.wanmi.sbc.setting.api.response.operatedatalog.OperateDataLogByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>系统日志查询服务Provider</p>
 * @author guanfl
 * @date 2020-04-21 14:57:15
 */
@FeignClient(value = "${application.setting.name}", contextId = "OperateDataLogQueryProvider")
public interface OperateDataLogQueryProvider {

	/**
	 * 分页查询系统日志API
	 *
	 * @author guanfl
	 * @param operateDataLogPageReq 分页请求参数和筛选对象 {@link OperateDataLogPageRequest}
	 * @return 系统日志分页列表信息 {@link OperateDataLogPageResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/operatedatalog/page")
	BaseResponse<OperateDataLogPageResponse> page(@RequestBody @Valid OperateDataLogPageRequest operateDataLogPageReq);

	/**
	 * 列表查询系统日志API
	 *
	 * @author guanfl
	 * @param operateDataLogListReq 列表请求参数和筛选对象 {@link OperateDataLogListRequest}
	 * @return 系统日志的列表信息 {@link OperateDataLogListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/operatedatalog/list")
	BaseResponse<OperateDataLogListResponse> list(@RequestBody @Valid OperateDataLogListRequest operateDataLogListReq);

	/**
	 * 单个查询系统日志API
	 *
	 * @author guanfl
	 * @param operateDataLogByIdRequest 单个查询系统日志请求参数 {@link OperateDataLogByIdRequest}
	 * @return 系统日志详情 {@link OperateDataLogByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/operatedatalog/get-by-id")
	BaseResponse<OperateDataLogByIdResponse> getById(@RequestBody @Valid OperateDataLogByIdRequest operateDataLogByIdRequest);

}

