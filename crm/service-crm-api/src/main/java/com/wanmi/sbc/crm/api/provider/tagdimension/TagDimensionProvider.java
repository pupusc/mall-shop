package com.wanmi.sbc.crm.api.provider.tagdimension;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.tagdimension.TagDimensionAddRequest;
import com.wanmi.sbc.crm.api.response.tagdimension.TagDimensionAddResponse;
import com.wanmi.sbc.crm.api.request.tagdimension.TagDimensionModifyRequest;
import com.wanmi.sbc.crm.api.response.tagdimension.TagDimensionModifyResponse;
import com.wanmi.sbc.crm.api.request.tagdimension.TagDimensionDelByIdRequest;
import com.wanmi.sbc.crm.api.request.tagdimension.TagDimensionDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>标签维度保存服务Provider</p>
 * @author dyt
 * @date 2020-03-12 16:00:30
 */
@FeignClient(value = "${application.crm.name}", contextId = "TagDimensionProvider")
public interface TagDimensionProvider {

	/**
	 * 新增标签维度API
	 *
	 * @author dyt
	 * @param tagDimensionAddRequest 标签维度新增参数结构 {@link TagDimensionAddRequest}
	 * @return 新增的标签维度信息 {@link TagDimensionAddResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/tagdimension/add")
	BaseResponse<TagDimensionAddResponse> add(@RequestBody @Valid TagDimensionAddRequest tagDimensionAddRequest);

	/**
	 * 修改标签维度API
	 *
	 * @author dyt
	 * @param tagDimensionModifyRequest 标签维度修改参数结构 {@link TagDimensionModifyRequest}
	 * @return 修改的标签维度信息 {@link TagDimensionModifyResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/tagdimension/modify")
	BaseResponse<TagDimensionModifyResponse> modify(@RequestBody @Valid TagDimensionModifyRequest
                                                            tagDimensionModifyRequest);

	/**
	 * 单个删除标签维度API
	 *
	 * @author dyt
	 * @param tagDimensionDelByIdRequest 单个删除参数结构 {@link TagDimensionDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/tagdimension/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid TagDimensionDelByIdRequest tagDimensionDelByIdRequest);

	/**
	 * 批量删除标签维度API
	 *
	 * @author dyt
	 * @param tagDimensionDelByIdListRequest 批量删除参数结构 {@link TagDimensionDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/tagdimension/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid TagDimensionDelByIdListRequest tagDimensionDelByIdListRequest);

}

