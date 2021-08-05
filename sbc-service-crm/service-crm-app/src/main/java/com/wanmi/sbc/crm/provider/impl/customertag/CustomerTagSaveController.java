package com.wanmi.sbc.crm.provider.impl.customertag;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.crm.api.constant.CustomerTagErrorCode;
import com.wanmi.sbc.crm.api.request.customertagrel.CustomerTagRelQueryRequest;
import com.wanmi.sbc.crm.customertagrel.service.CustomerTagRelService;
import com.wanmi.sbc.crm.customgroup.service.CustomGroupService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.customertag.CustomerTagSaveProvider;
import com.wanmi.sbc.crm.api.request.customertag.CustomerTagAddRequest;
import com.wanmi.sbc.crm.api.response.customertag.CustomerTagAddResponse;
import com.wanmi.sbc.crm.api.request.customertag.CustomerTagModifyRequest;
import com.wanmi.sbc.crm.api.response.customertag.CustomerTagModifyResponse;
import com.wanmi.sbc.crm.api.request.customertag.CustomerTagDelByIdRequest;
import com.wanmi.sbc.crm.api.request.customertag.CustomerTagDelByIdListRequest;
import com.wanmi.sbc.crm.customertag.service.CustomerTagService;
import com.wanmi.sbc.crm.customertag.model.root.CustomerTag;
import javax.validation.Valid;
import java.util.Optional;

/**
 * <p>会员标签保存服务接口实现</p>
 * @author zhanglingke
 * @date 2019-10-14 11:19:11
 */
@RestController
@Validated
public class CustomerTagSaveController implements CustomerTagSaveProvider {
	@Autowired
	private CustomerTagService customerTagService;
	@Autowired
	private CustomGroupService customGroupService;

	@Autowired
	private CustomerTagRelService customerTagRelService;

	@Override
	public BaseResponse<CustomerTagAddResponse> add(@RequestBody @Valid CustomerTagAddRequest customerTagAddRequest) {
		if(customerTagService.getByName(customerTagAddRequest.getName()).isPresent()){
			throw new SbcRuntimeException(CustomerTagErrorCode.TagName_HAS_EXISTS);
		}
		CustomerTag customerTag = new CustomerTag();
		KsBeanUtil.copyPropertiesThird(customerTagAddRequest, customerTag);
		return BaseResponse.success(new CustomerTagAddResponse(
				customerTagService.wrapperVo(customerTagService.add(customerTag))));
	}

	@Override
	public BaseResponse<CustomerTagModifyResponse> modify(@RequestBody @Valid CustomerTagModifyRequest customerTagModifyRequest) {
		Optional<CustomerTag> customerTagOptional = customerTagService.getByName(customerTagModifyRequest.getName());
		if(customerTagOptional.isPresent() && !customerTagOptional.get().getId().equals(customerTagModifyRequest.getId())){
			throw new SbcRuntimeException(CustomerTagErrorCode.TagName_HAS_EXISTS);
		}
		CustomerTag customerTag = new CustomerTag();
		KsBeanUtil.copyPropertiesThird(customerTagModifyRequest, customerTag);
		return BaseResponse.success(new CustomerTagModifyResponse(
				customerTagService.wrapperVo(customerTagService.modify(customerTag))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid CustomerTagDelByIdRequest customerTagDelByIdRequest) {
		if(customGroupService.checkCustomerTag(customerTagDelByIdRequest.getId())>0 ||
				CollectionUtils.isNotEmpty(customerTagRelService.list(CustomerTagRelQueryRequest.builder()
						.tagId(customerTagDelByIdRequest.getId()).build()))){
			throw new SbcRuntimeException(CustomerTagErrorCode.TAG_EXISTS_CUSTOM_GROUP);
		}
		customerTagService.deleteById(customerTagDelByIdRequest.getId());
		return BaseResponse.success("");
	}

	//如果要有批量删除接口，需要重新考虑会员标签存在自定义人群的校验问题
	/*@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid CustomerTagDelByIdListRequest customerTagDelByIdListRequest) {
		customerTagService.deleteByIdList(customerTagDelByIdListRequest.getIdList());
		return BaseResponse.success("");
	}*/

}

