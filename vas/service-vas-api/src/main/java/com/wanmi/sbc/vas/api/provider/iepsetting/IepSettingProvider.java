package com.wanmi.sbc.vas.api.provider.iepsetting;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.vas.api.request.iepsetting.IepSettingAddRequest;
import com.wanmi.sbc.vas.api.response.iepsetting.IepSettingAddResponse;
import com.wanmi.sbc.vas.api.request.iepsetting.IepSettingModifyRequest;
import com.wanmi.sbc.vas.api.response.iepsetting.IepSettingModifyResponse;
import com.wanmi.sbc.vas.api.request.iepsetting.IepSettingDelByIdRequest;
import com.wanmi.sbc.vas.api.request.iepsetting.IepSettingDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>企业购设置保存服务Provider</p>
 * @author 宋汉林
 * @date 2020-03-02 20:15:04
 */
@FeignClient(value = "${application.vas.name}", contextId = "IepSettingProvider")
public interface IepSettingProvider {

	/**
	 * 新增企业购设置API
	 *
	 * @author 宋汉林
	 * @param iepSettingAddRequest 企业购设置新增参数结构 {@link IepSettingAddRequest}
	 * @return 新增的企业购设置信息 {@link IepSettingAddResponse}
	 */
	@PostMapping("/vas/${application.vas.version}/iepsetting/add")
	BaseResponse<IepSettingAddResponse> add(@RequestBody @Valid IepSettingAddRequest iepSettingAddRequest);

	/**
	 * 修改企业购设置API
	 *
	 * @author 宋汉林
	 * @param iepSettingModifyRequest 企业购设置修改参数结构 {@link IepSettingModifyRequest}
	 * @return 修改的企业购设置信息 {@link IepSettingModifyResponse}
	 */
	@PostMapping("/vas/${application.vas.version}/iepsetting/modify")
	BaseResponse<IepSettingModifyResponse> modify(@RequestBody @Valid IepSettingModifyRequest iepSettingModifyRequest);

	/**
	 * 单个删除企业购设置API
	 *
	 * @author 宋汉林
	 * @param iepSettingDelByIdRequest 单个删除参数结构 {@link IepSettingDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/vas/${application.vas.version}/iepsetting/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid IepSettingDelByIdRequest iepSettingDelByIdRequest);

	/**
	 * 批量删除企业购设置API
	 *
	 * @author 宋汉林
	 * @param iepSettingDelByIdListRequest 批量删除参数结构 {@link IepSettingDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/vas/${application.vas.version}/iepsetting/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid IepSettingDelByIdListRequest iepSettingDelByIdListRequest);

}

