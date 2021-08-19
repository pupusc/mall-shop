package com.wanmi.sbc.marketing.provider.impl.grouponcate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.provider.grouponcate.GrouponCateQueryProvider;
import com.wanmi.sbc.marketing.api.request.grouponcate.GrouponCateByIdRequest;
import com.wanmi.sbc.marketing.api.request.grouponcate.GrouponCateByIdsRequest;
import com.wanmi.sbc.marketing.api.response.grouponcate.GrouponCateByIdResponse;
import com.wanmi.sbc.marketing.api.response.grouponcate.GrouponCateListResponse;
import com.wanmi.sbc.marketing.bean.vo.GrouponCateVO;
import com.wanmi.sbc.marketing.grouponcate.model.root.GrouponCate;
import com.wanmi.sbc.marketing.grouponcate.service.GrouponCateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>拼团活动信息表查询服务接口实现</p>
 * @author groupon
 * @date 2019-05-15 14:13:58
 */
@RestController
@Validated
public class GrouponCateQueryController implements GrouponCateQueryProvider {
	@Autowired
	private GrouponCateService grouponCateService;

	@Override
	public BaseResponse<GrouponCateListResponse> list() {
		List<GrouponCate> grouponCateList = grouponCateService.list();
		List<GrouponCateVO> newList = grouponCateList.stream().map(entity -> grouponCateService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new GrouponCateListResponse(newList));
	}

	@Override
	public BaseResponse<GrouponCateByIdResponse> getById(@RequestBody @Valid GrouponCateByIdRequest grouponCateByIdRequest) {
		GrouponCate grouponCate = grouponCateService.getById(grouponCateByIdRequest.getGrouponCateId());
		return BaseResponse.success(new GrouponCateByIdResponse(grouponCateService.wrapperVo(grouponCate)));
	}

	/**
	 * 批量查询拼团分类信息表API
	 *
	 * @param request
	 * @return 拼团分类信息表详情 {@link GrouponCateByIdResponse}
	 */
	@Override
	public BaseResponse<GrouponCateListResponse> getByIds(@Valid GrouponCateByIdsRequest request) {
		return BaseResponse.success(
				GrouponCateListResponse.builder().grouponCateVOList(
						grouponCateService.findByGrouponCateIdIn(request.getGrouponCateIds()).stream()
								.map(item->grouponCateService.wrapperVo(item)).collect(Collectors.toList())).build());
	}



}

