package com.wanmi.sbc.vas.api.provider.iepsetting;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.vas.api.request.iepsetting.IepSettingPageRequest;
import com.wanmi.sbc.vas.api.response.iepsetting.IepSettingPageResponse;
import com.wanmi.sbc.vas.api.request.iepsetting.IepSettingListRequest;
import com.wanmi.sbc.vas.api.response.iepsetting.IepSettingListResponse;
import com.wanmi.sbc.vas.api.request.iepsetting.IepSettingByIdRequest;
import com.wanmi.sbc.vas.api.response.iepsetting.IepSettingByIdResponse;
import com.wanmi.sbc.vas.api.response.iepsetting.IepSettingTopOneResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>企业购设置查询服务Provider</p>
 * @author 宋汉林
 * @date 2020-03-02 20:15:04
 */
@FeignClient(value = "${application.vas.name}", contextId = "IepSettingQueryProvider")
public interface IepSettingQueryProvider {

	/**
	 * 分页查询企业购设置API
	 *
	 * @author 宋汉林
	 * @param iepSettingPageReq 分页请求参数和筛选对象 {@link IepSettingPageRequest}
	 * @return 企业购设置分页列表信息 {@link IepSettingPageResponse}
	 */
	@PostMapping("/vas/${application.vas.version}/iepsetting/page")
	BaseResponse<IepSettingPageResponse> page(@RequestBody @Valid IepSettingPageRequest iepSettingPageReq);

	/**
	 * 列表查询企业购设置API
	 *
	 * @author 宋汉林
	 * @param iepSettingListReq 列表请求参数和筛选对象 {@link IepSettingListRequest}
	 * @return 企业购设置的列表信息 {@link IepSettingListResponse}
	 */
	@PostMapping("/vas/${application.vas.version}/iepsetting/list")
	BaseResponse<IepSettingListResponse> list(@RequestBody @Valid IepSettingListRequest iepSettingListReq);

	/**
	 * 单个查询企业购设置API
	 *
	 * @author 宋汉林
	 * @param iepSettingByIdRequest 单个查询企业购设置请求参数 {@link IepSettingByIdRequest}
	 * @return 企业购设置详情 {@link IepSettingByIdResponse}
	 */
	@PostMapping("/vas/${application.vas.version}/iepsetting/get-by-id")
	BaseResponse<IepSettingByIdResponse> getById(@RequestBody @Valid IepSettingByIdRequest iepSettingByIdRequest);

	/**
	 * 查询第一个企业购设置信息
	 * @return
	 */
	@PostMapping("/vas/${application.vas.version}/iepsetting/get-top-one")
	BaseResponse<IepSettingTopOneResponse> findTopOne();

	/**
	 * 缓存企业购设置信息
	 * @return
	 */
	@PostMapping("/vas/${application.vas.version}/iepsetting/cache")
	BaseResponse<IepSettingTopOneResponse> cacheIepSetting();
}

