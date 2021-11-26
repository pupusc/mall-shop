package com.wanmi.sbc.crm.provider.impl.customertagrel;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.customertagrel.CustomerTagRelSaveProvider;
import com.wanmi.sbc.crm.api.request.customertagrel.CustomerTagRelAddRequest;
import com.wanmi.sbc.crm.api.response.customertagrel.CustomerTagRelAddResponse;
import com.wanmi.sbc.crm.api.request.customertagrel.CustomerTagRelModifyRequest;
import com.wanmi.sbc.crm.api.response.customertagrel.CustomerTagRelModifyResponse;
import com.wanmi.sbc.crm.api.request.customertagrel.CustomerTagRelDelByIdRequest;
import com.wanmi.sbc.crm.api.request.customertagrel.CustomerTagRelDelByIdListRequest;
import com.wanmi.sbc.crm.customertagrel.service.CustomerTagRelService;
import com.wanmi.sbc.crm.customertagrel.model.root.CustomerTagRel;
import javax.validation.Valid;
import java.util.stream.Collectors;

/**
 * <p>会员标签关联保存服务接口实现</p>
 * @author dyt
 * @date 2019-11-12 14:49:08
 */
@RestController
@Validated
public class CustomerTagRelSaveController implements CustomerTagRelSaveProvider {
	@Autowired
	private CustomerTagRelService customerTagRelService;

	@Override
	public BaseResponse add(@RequestBody @Valid CustomerTagRelAddRequest customerTagRelAddRequest) {
		if(CollectionUtils.isNotEmpty(customerTagRelAddRequest.getTagIds())){
			customerTagRelService.add(customerTagRelAddRequest.getTagIds().stream().map(tagId -> {
				CustomerTagRel customerTagRel = new CustomerTagRel();
				KsBeanUtil.copyPropertiesThird(customerTagRelAddRequest, customerTagRel);
				customerTagRel.setTagId(tagId);
				return customerTagRel;
			}).collect(Collectors.toList()));
		} else {
			customerTagRelService.deleteByCustomerId(customerTagRelAddRequest.getCustomerId());
		}
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse<CustomerTagRelModifyResponse> modify(@RequestBody @Valid CustomerTagRelModifyRequest customerTagRelModifyRequest) {
		CustomerTagRel customerTagRel = new CustomerTagRel();
		KsBeanUtil.copyPropertiesThird(customerTagRelModifyRequest, customerTagRel);
		return BaseResponse.success(new CustomerTagRelModifyResponse(
				customerTagRelService.wrapperVo(customerTagRelService.modify(customerTagRel))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid CustomerTagRelDelByIdRequest customerTagRelDelByIdRequest) {
		customerTagRelService.deleteById(customerTagRelDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid CustomerTagRelDelByIdListRequest customerTagRelDelByIdListRequest) {
		customerTagRelService.deleteByIdList(customerTagRelDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

