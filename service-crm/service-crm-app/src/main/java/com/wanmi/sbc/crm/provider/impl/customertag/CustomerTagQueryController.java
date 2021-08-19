package com.wanmi.sbc.crm.provider.impl.customertag;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.customertag.CustomerTagQueryProvider;
import com.wanmi.sbc.crm.api.request.customertag.CustomerTagPageRequest;
import com.wanmi.sbc.crm.api.request.customertag.CustomerTagQueryRequest;
import com.wanmi.sbc.crm.api.response.customertag.CustomerTagPageResponse;
import com.wanmi.sbc.crm.api.request.customertag.CustomerTagListRequest;
import com.wanmi.sbc.crm.api.response.customertag.CustomerTagListResponse;
import com.wanmi.sbc.crm.api.request.customertag.CustomerTagByIdRequest;
import com.wanmi.sbc.crm.api.response.customertag.CustomerTagByIdResponse;
import com.wanmi.sbc.crm.bean.vo.CustomerTagVO;
import com.wanmi.sbc.crm.customertag.service.CustomerTagService;
import com.wanmi.sbc.crm.customertag.model.root.CustomerTag;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>会员标签查询服务接口实现</p>
 * @author zhanglingke
 * @date 2019-10-14 11:19:11
 */
@RestController
@Validated
public class CustomerTagQueryController implements CustomerTagQueryProvider {
	@Autowired
	private CustomerTagService customerTagService;

	@Override
	public BaseResponse<CustomerTagPageResponse> page(@RequestBody @Valid CustomerTagPageRequest customerTagPageReq) {
		CustomerTagQueryRequest queryReq = new CustomerTagQueryRequest();
		KsBeanUtil.copyPropertiesThird(customerTagPageReq, queryReq);
		Page<CustomerTag> customerTagPage = customerTagService.page(queryReq);
		Page<CustomerTagVO> newPage = customerTagPage.map(entity -> customerTagService.wrapperVo(entity));
		MicroServicePage<CustomerTagVO> microPage = new MicroServicePage<>(newPage, customerTagPageReq.getPageable());
		CustomerTagPageResponse finalRes = new CustomerTagPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<CustomerTagListResponse> list(@RequestBody @Valid CustomerTagListRequest customerTagListReq) {
		CustomerTagQueryRequest queryReq = new CustomerTagQueryRequest();
		KsBeanUtil.copyPropertiesThird(customerTagListReq, queryReq);
		List<CustomerTag> customerTagList = customerTagService.list(queryReq);
		List<CustomerTagVO> newList = customerTagList.stream().map(entity -> customerTagService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new CustomerTagListResponse(newList));
	}

	@Override
	public BaseResponse<CustomerTagByIdResponse> getById(@RequestBody @Valid CustomerTagByIdRequest customerTagByIdRequest) {
		CustomerTag customerTag = customerTagService.getById(customerTagByIdRequest.getId());
		return BaseResponse.success(new CustomerTagByIdResponse(customerTagService.wrapperVo(customerTag)));
	}

}

