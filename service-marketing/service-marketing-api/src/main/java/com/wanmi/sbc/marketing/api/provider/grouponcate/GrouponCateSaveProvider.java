package com.wanmi.sbc.marketing.api.provider.grouponcate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.grouponcate.GrouponCateAddRequest;
import com.wanmi.sbc.marketing.api.request.grouponcate.GrouponCateModifyRequest;
import com.wanmi.sbc.marketing.api.request.grouponcate.GrouponCateDelByIdRequest;
import com.wanmi.sbc.marketing.api.request.grouponcate.GrouponCateSortRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>拼团分类信息表保存服务Provider</p>
 * @author groupon
 * @date 2019-05-15 14:13:58
 */
@FeignClient(value = "${application.marketing.name}", contextId = "GrouponCateSaveProvider")
public interface GrouponCateSaveProvider {

	/**
	 * 新增拼团分类信息表API
	 *
	 * @author groupon
	 * @param grouponCateAddRequest 拼团分类信息表新增参数结构 {@link GrouponCateAddRequest}
	 * @return 新增的拼团分类信息表信息
	 */
	@PostMapping("/marketing/${application.marketing.version}/grouponcate/add")
	BaseResponse add(@RequestBody @Valid GrouponCateAddRequest grouponCateAddRequest);

	/**
	 * 修改拼团分类信息表API
	 *
	 * @author groupon
	 * @param grouponCateModifyRequest 拼团分类信息表修改参数结构 {@link GrouponCateModifyRequest}
	 * @return 修改的拼团分类信息表信息
	 */
	@PostMapping("/marketing/${application.marketing.version}/grouponcate/modify")
	BaseResponse modify(@RequestBody @Valid GrouponCateModifyRequest
                                                           grouponCateModifyRequest);

	/**
	 * 单个删除拼团分类信息表API
	 *
	 * @author groupon
	 * @param grouponCateDelByIdRequest 单个删除参数结构 {@link GrouponCateDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/grouponcate/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid GrouponCateDelByIdRequest grouponCateDelByIdRequest);

	/**
	 * 拖拽排序拼团分类信息表API
	 *
	 * @author groupon
	 * @param grouponCateSortRequest 拖拽排序拼团分类信息结构 {@link GrouponCateDelByIdRequest}
	 * @return 排序结果 {@link BaseResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/grouponcate/drag-sort")
	BaseResponse dragSort(@RequestBody @Valid GrouponCateSortRequest grouponCateSortRequest);

}

