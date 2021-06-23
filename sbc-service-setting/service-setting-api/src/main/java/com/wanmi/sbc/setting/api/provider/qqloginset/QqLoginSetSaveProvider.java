package com.wanmi.sbc.setting.api.provider.qqloginset;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.qqloginset.QqLoginSetAddRequest;
import com.wanmi.sbc.setting.api.response.qqloginset.QqLoginSetAddResponse;
import com.wanmi.sbc.setting.api.request.qqloginset.QqLoginSetModifyRequest;
import com.wanmi.sbc.setting.api.response.qqloginset.QqLoginSetModifyResponse;
import com.wanmi.sbc.setting.api.request.qqloginset.QqLoginSetDelByIdRequest;
import com.wanmi.sbc.setting.api.request.qqloginset.QqLoginSetDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>qq登录信息保存服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:11:28
 */
@FeignClient(value = "${application.setting.name}", contextId = "QqLoginSetSaveProvider")
public interface QqLoginSetSaveProvider {

	/**
	 * 新增qq登录信息API
	 *
	 * @author lq
	 * @param qqLoginSetAddRequest qq登录信息新增参数结构 {@link QqLoginSetAddRequest}
	 * @return 新增的qq登录信息信息 {@link QqLoginSetAddResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/qqloginset/add")
	BaseResponse<QqLoginSetAddResponse> add(@RequestBody @Valid QqLoginSetAddRequest qqLoginSetAddRequest);

	/**
	 * 修改qq登录信息API
	 *
	 * @author lq
	 * @param qqLoginSetModifyRequest qq登录信息修改参数结构 {@link QqLoginSetModifyRequest}
	 * @return 修改的qq登录信息信息 {@link QqLoginSetModifyResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/qqloginset/modify")
	BaseResponse<QqLoginSetModifyResponse> modify(@RequestBody @Valid QqLoginSetModifyRequest qqLoginSetModifyRequest);

	/**
	 * 单个删除qq登录信息API
	 *
	 * @author lq
	 * @param qqLoginSetDelByIdRequest 单个删除参数结构 {@link QqLoginSetDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/qqloginset/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid QqLoginSetDelByIdRequest qqLoginSetDelByIdRequest);

	/**
	 * 批量删除qq登录信息API
	 *
	 * @author lq
	 * @param qqLoginSetDelByIdListRequest 批量删除参数结构 {@link QqLoginSetDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/qqloginset/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid QqLoginSetDelByIdListRequest qqLoginSetDelByIdListRequest);

}

