package com.wanmi.sbc.goods.api.provider.thirdgoodscate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.thirdgoodscate.*;
import com.wanmi.sbc.goods.api.response.thirdgoodscate.ThirdGoodsCatePageResponse;
import com.wanmi.sbc.goods.api.response.thirdgoodscate.ThirdGoodsCateListResponse;
import com.wanmi.sbc.goods.api.response.thirdgoodscate.ThirdGoodsCateByIdResponse;
import com.wanmi.sbc.goods.bean.dto.ThirdGoodsCateRelDTO;
import com.wanmi.sbc.goods.bean.vo.ThirdGoodsCateRelVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>查询服务Provider</p>
 * @author 
 * @date 2020-08-17 14:46:43
 */
@FeignClient(value = "${application.goods.name}", contextId = "ThirdGoodsCateQueryProvider")
public interface ThirdGoodsCateQueryProvider {

	/**
	 * 分页查询API
	 *
	 * @author 
	 * @param thirdGoodsCatePageReq 分页请求参数和筛选对象 {@link ThirdGoodsCatePageRequest}
	 * @return 分页列表信息 {@link ThirdGoodsCatePageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/thirdgoodscate/page")
	BaseResponse<ThirdGoodsCatePageResponse> page(@RequestBody @Valid ThirdGoodsCatePageRequest thirdGoodsCatePageReq);

	/**
	 * 列表查询API
	 *
	 * @author 
	 * @param thirdGoodsCateListReq 列表请求参数和筛选对象 {@link ThirdGoodsCateListRequest}
	 * @return 的列表信息 {@link ThirdGoodsCateListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/thirdgoodscate/list")
	BaseResponse<ThirdGoodsCateListResponse> list(@RequestBody @Valid ThirdGoodsCateListRequest thirdGoodsCateListReq);
	/**
	 * 查询所有三方类目并关联平台类目
	 *
	 * @author
	 * @return 的列表信息 {@link ThirdGoodsCateListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/thirdgoodscate/list/rel")
	BaseResponse<List<ThirdGoodsCateRelVO>> listRel(@RequestBody @Valid CateRelRequest request);

	/**
	 * 单个查询API
	 *
	 * @author 
	 * @param thirdGoodsCateByIdRequest 单个查询请求参数 {@link ThirdGoodsCateByIdRequest}
	 * @return 详情 {@link ThirdGoodsCateByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/thirdgoodscate/get-by-id")
	BaseResponse<ThirdGoodsCateByIdResponse> getById(@RequestBody @Valid ThirdGoodsCateByIdRequest thirdGoodsCateByIdRequest);

	/**
	 * 根据类目父id查询子类目
	 * @param cateRelByParentIdRequest
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/thirdgoodscate/getRelByParentId")
	BaseResponse<List<ThirdGoodsCateRelDTO>> getRelByParentId(@RequestBody CateRelByParentIdRequest cateRelByParentIdRequest);
}

