package com.wanmi.sbc.crm.provider.impl.customertagrel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.customertagrel.CustomerTagRelQueryProvider;
import com.wanmi.sbc.crm.api.request.customertag.CustomerTagQueryRequest;
import com.wanmi.sbc.crm.api.request.customertagrel.CustomerTagRelByIdRequest;
import com.wanmi.sbc.crm.api.request.customertagrel.CustomerTagRelListRequest;
import com.wanmi.sbc.crm.api.request.customertagrel.CustomerTagRelPageRequest;
import com.wanmi.sbc.crm.api.request.customertagrel.CustomerTagRelQueryRequest;
import com.wanmi.sbc.crm.api.response.customertagrel.CustomerTagRelByIdResponse;
import com.wanmi.sbc.crm.api.response.customertagrel.CustomerTagRelListResponse;
import com.wanmi.sbc.crm.api.response.customertagrel.CustomerTagRelPageResponse;
import com.wanmi.sbc.crm.bean.vo.CustomerTagRelVO;
import com.wanmi.sbc.crm.customertag.model.root.CustomerTag;
import com.wanmi.sbc.crm.customertag.service.CustomerTagService;
import com.wanmi.sbc.crm.customertagrel.model.root.CustomerTagRel;
import com.wanmi.sbc.crm.customertagrel.service.CustomerTagRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>会员标签关联查询服务接口实现</p>
 * @author dyt
 * @date 2019-11-12 14:49:08
 */
@RestController
@Validated
public class CustomerTagRelQueryController implements CustomerTagRelQueryProvider {
	@Autowired
	private CustomerTagRelService customerTagRelService;

	@Autowired
	private CustomerTagService customerTagService;

	@Override
	public BaseResponse<CustomerTagRelPageResponse> page(@RequestBody @Valid CustomerTagRelPageRequest customerTagRelPageReq) {
		CustomerTagRelQueryRequest queryReq = new CustomerTagRelQueryRequest();
		KsBeanUtil.copyPropertiesThird(customerTagRelPageReq, queryReq);
		Page<CustomerTagRel> customerTagRelPage = customerTagRelService.page(queryReq);
		Page<CustomerTagRelVO> newPage = customerTagRelPage.map(entity -> customerTagRelService.wrapperVo(entity));
		MicroServicePage<CustomerTagRelVO> microPage = new MicroServicePage<>(newPage, customerTagRelPageReq.getPageable());
		CustomerTagRelPageResponse finalRes = new CustomerTagRelPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<CustomerTagRelListResponse> list(@RequestBody @Valid CustomerTagRelListRequest customerTagRelListReq) {
        CustomerTagRelQueryRequest queryReq = new CustomerTagRelQueryRequest();
        KsBeanUtil.copyPropertiesThird(customerTagRelListReq, queryReq);
        List<CustomerTagRel> customerTagRelList = customerTagRelService.list(queryReq);
        Map<Long, String> tagNameMap = new HashMap<>();
        if (Boolean.TRUE.equals(customerTagRelListReq.getShowTagName())) {
            List<Long> tagIds = customerTagRelList.stream().map(CustomerTagRel::getTagId).collect(Collectors.toList());
            tagNameMap.putAll(customerTagService.list(CustomerTagQueryRequest.builder().delFlag(DeleteFlag.NO)
                    .idList(tagIds).build()).stream().collect(Collectors.toMap(CustomerTag::getId, CustomerTag::getName)));
        }
        List<CustomerTagRelVO> newList = customerTagRelList.stream()
                .map(entity -> {
                    CustomerTagRelVO vo = customerTagRelService.wrapperVo(entity);
                    vo.setTagName(tagNameMap.get(vo.getTagId()));
                    return vo;
                }).collect(Collectors.toList());
        return BaseResponse.success(new CustomerTagRelListResponse(newList));
    }

	@Override
	public BaseResponse<CustomerTagRelByIdResponse> getById(@RequestBody @Valid CustomerTagRelByIdRequest customerTagRelByIdRequest) {
		CustomerTagRel customerTagRel = customerTagRelService.getById(customerTagRelByIdRequest.getId());
		return BaseResponse.success(new CustomerTagRelByIdResponse(customerTagRelService.wrapperVo(customerTagRel)));
	}

}

