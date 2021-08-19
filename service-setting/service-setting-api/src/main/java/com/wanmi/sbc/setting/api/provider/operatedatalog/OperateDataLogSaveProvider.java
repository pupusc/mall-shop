package com.wanmi.sbc.setting.api.provider.operatedatalog;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.operatedatalog.*;
import com.wanmi.sbc.setting.api.response.operatedatalog.OperateDataLogAddResponse;
import com.wanmi.sbc.setting.api.response.operatedatalog.OperateDataLogModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>系统日志保存服务Provider</p>
 * @author guanfl
 * @date 2020-04-21 14:57:15
 */
@FeignClient(value = "${application.setting.name}", contextId = "OperateDataLogSaveProvider")
public interface OperateDataLogSaveProvider {

	/**
	 * 新增系统日志API
	 *
	 * @author guanfl
	 * @param operateDataLogAddRequest 系统日志新增参数结构 {@link OperateDataLogAddRequest}
	 * @return 新增的系统日志信息 {@link OperateDataLogAddResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/operatedatalog/add")
	BaseResponse<OperateDataLogAddResponse> add(@RequestBody @Valid OperateDataLogAddRequest operateDataLogAddRequest);

	/**
	 * 修改系统日志API
	 *
	 * @author guanfl
	 * @param operateDataLogModifyRequest 系统日志修改参数结构 {@link OperateDataLogModifyRequest}
	 * @return 修改的系统日志信息 {@link OperateDataLogModifyResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/operatedatalog/modify")
	BaseResponse<OperateDataLogModifyResponse> modify(@RequestBody @Valid OperateDataLogModifyRequest operateDataLogModifyRequest);

	/**
	 * 单个删除系统日志API
	 *
	 * @author guanfl
	 * @param operateDataLogDelByIdRequest 单个删除参数结构 {@link OperateDataLogDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/operatedatalog/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid OperateDataLogDelByIdRequest operateDataLogDelByIdRequest);

	/**
	 * 
	 * @param operateDataLogDelByOperateIdRequest
	 * @return
	 */
	@PostMapping("/setting/${application.setting.version}/operatedatalog/delete-by-operate-id")
	BaseResponse deleteByOperateId(@RequestBody @Valid OperateDataLogDelByOperateIdRequest operateDataLogDelByOperateIdRequest);


	/**
	 * 批量删除系统日志API
	 *
	 * @author guanfl
	 * @param operateDataLogDelByIdListRequest 批量删除参数结构 {@link OperateDataLogDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/operatedatalog/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid OperateDataLogDelByIdListRequest operateDataLogDelByIdListRequest);

	/**
	 * 新增系统日志API
	 *
	 * @author guanfl
	 * @param operateDataLogSynRequest 系统日志新增参数结构 {@link OperateDataLogAddRequest}
	 * @return 新增的系统日志信息 {@link OperateDataLogAddResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/operatedatalog/syn")
	BaseResponse<OperateDataLogAddResponse> synDataLog(@RequestBody @Valid OperateDataLogSynRequest operateDataLogSynRequest);

}

