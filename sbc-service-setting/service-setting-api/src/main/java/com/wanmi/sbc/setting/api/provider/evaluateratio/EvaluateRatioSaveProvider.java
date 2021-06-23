package com.wanmi.sbc.setting.api.provider.evaluateratio;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.evaluateratio.EvaluateRatioAddRequest;
import com.wanmi.sbc.setting.api.response.evaluateratio.EvaluateRatioAddResponse;
import com.wanmi.sbc.setting.api.request.evaluateratio.EvaluateRatioModifyRequest;
import com.wanmi.sbc.setting.api.response.evaluateratio.EvaluateRatioModifyResponse;
import com.wanmi.sbc.setting.api.request.evaluateratio.EvaluateRatioDelByIdRequest;
import com.wanmi.sbc.setting.api.request.evaluateratio.EvaluateRatioDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>商品评价系数设置保存服务Provider</p>
 * @author liutao
 * @date 2019-02-27 15:53:40
 */
@FeignClient(value = "${application.setting.name}", contextId = "EvaluateRatioSaveProvider")
public interface EvaluateRatioSaveProvider {

	/**
	 * 新增商品评价系数设置API
	 *
	 * @author liutao
	 * @param evaluateRatioAddRequest 商品评价系数设置新增参数结构 {@link EvaluateRatioAddRequest}
	 * @return 新增的商品评价系数设置信息 {@link EvaluateRatioAddResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/evaluateratio/add")
	BaseResponse<EvaluateRatioAddResponse> add(@RequestBody @Valid EvaluateRatioAddRequest evaluateRatioAddRequest);

	/**
	 * 修改商品评价系数设置API
	 *
	 * @author liutao
	 * @param evaluateRatioModifyRequest 商品评价系数设置修改参数结构 {@link EvaluateRatioModifyRequest}
	 * @return 修改的商品评价系数设置信息 {@link EvaluateRatioModifyResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/evaluateratio/modify")
	BaseResponse<EvaluateRatioModifyResponse> modify(@RequestBody @Valid EvaluateRatioModifyRequest evaluateRatioModifyRequest);

	/**
	 * 单个删除商品评价系数设置API
	 *
	 * @author liutao
	 * @param evaluateRatioDelByIdRequest 单个删除参数结构 {@link EvaluateRatioDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/evaluateratio/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid EvaluateRatioDelByIdRequest evaluateRatioDelByIdRequest);

	/**
	 * 批量删除商品评价系数设置API
	 *
	 * @author liutao
	 * @param evaluateRatioDelByIdListRequest 批量删除参数结构 {@link EvaluateRatioDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/evaluateratio/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid EvaluateRatioDelByIdListRequest evaluateRatioDelByIdListRequest);

}

