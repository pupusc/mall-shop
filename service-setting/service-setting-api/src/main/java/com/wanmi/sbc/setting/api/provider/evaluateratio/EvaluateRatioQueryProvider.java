package com.wanmi.sbc.setting.api.provider.evaluateratio;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.evaluateratio.EvaluateRatioPageRequest;
import com.wanmi.sbc.setting.api.response.evaluateratio.EvaluateRatioPageResponse;
import com.wanmi.sbc.setting.api.request.evaluateratio.EvaluateRatioListRequest;
import com.wanmi.sbc.setting.api.response.evaluateratio.EvaluateRatioListResponse;
import com.wanmi.sbc.setting.api.request.evaluateratio.EvaluateRatioByIdRequest;
import com.wanmi.sbc.setting.api.response.evaluateratio.EvaluateRatioByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>商品评价系数设置查询服务Provider</p>
 * @author liutao
 * @date 2019-02-27 15:53:40
 */
@FeignClient(value = "${application.setting.name}", contextId = "EvaluateRatioQueryProvider")
public interface EvaluateRatioQueryProvider {

	/**
	 * 分页查询商品评价系数设置API
	 *
	 * @author liutao
	 * @param evaluateRatioPageReq 分页请求参数和筛选对象 {@link EvaluateRatioPageRequest}
	 * @return 商品评价系数设置分页列表信息 {@link EvaluateRatioPageResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/evaluateratio/page")
	BaseResponse<EvaluateRatioPageResponse> page(@RequestBody @Valid EvaluateRatioPageRequest evaluateRatioPageReq);

	/**
	 * 列表查询商品评价系数设置API
	 *
	 * @author liutao
	 * @param evaluateRatioListReq 列表请求参数和筛选对象 {@link EvaluateRatioListRequest}
	 * @return 商品评价系数设置的列表信息 {@link EvaluateRatioListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/evaluateratio/list")
	BaseResponse<EvaluateRatioListResponse> list(@RequestBody @Valid EvaluateRatioListRequest evaluateRatioListReq);

	/**
	 * 单个查询商品评价系数设置API
	 *
	 * @author liutao
	 * @param evaluateRatioByIdRequest 单个查询商品评价系数设置请求参数 {@link EvaluateRatioByIdRequest}
	 * @return 商品评价系数设置详情 {@link EvaluateRatioByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/evaluateratio/get-by-id")
	BaseResponse<EvaluateRatioByIdResponse> getById(@RequestBody @Valid EvaluateRatioByIdRequest evaluateRatioByIdRequest);

	/**
	 * 单个查询商品评价系数设置API
	 *
	 * @author liutao
	 * @return 商品评价系数设置详情 {@link EvaluateRatioByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/evaluateratio/find-one")
	BaseResponse<EvaluateRatioByIdResponse> findOne();

}

