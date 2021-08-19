package com.wanmi.sbc.customer.provider.impl.levelrights;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.api.provider.levelrights.CustomerLevelRightsQueryProvider;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsQueryRequest;
import com.wanmi.sbc.customer.api.response.levelrights.CustomerLevelRightsListResponse;
import com.wanmi.sbc.customer.api.response.levelrights.CustomerLevelRightsPageResponse;
import com.wanmi.sbc.customer.api.response.levelrights.CustomerLevelRightsResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelRightsVO;
import com.wanmi.sbc.customer.levelrights.model.root.CustomerLevelRights;
import com.wanmi.sbc.customer.levelrights.service.CustomerLevelRightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>会员等级权益表查询服务接口实现</p>
 */
@RestController
@Validated
public class CustomerLevelRightsQueryController implements CustomerLevelRightsQueryProvider {
    @Autowired
    private CustomerLevelRightsService customerLevelRightsService;

    @Override
    public BaseResponse<CustomerLevelRightsPageResponse> page(@RequestBody @Valid CustomerLevelRightsQueryRequest customerLevelRightsPageReq) {
        Page<CustomerLevelRights> customerLevelRightsPage = customerLevelRightsService.page(customerLevelRightsPageReq);
        Page<CustomerLevelRightsVO> newPage = customerLevelRightsPage.map(entity -> customerLevelRightsService.wrapperVo(entity));
        MicroServicePage<CustomerLevelRightsVO> microPage = new MicroServicePage<>(newPage, customerLevelRightsPageReq.getPageable());
        CustomerLevelRightsPageResponse finalRes = new CustomerLevelRightsPageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<CustomerLevelRightsListResponse> list(@RequestBody @Valid CustomerLevelRightsQueryRequest customerLevelRightsListReq) {
        List<CustomerLevelRights> customerLevelRightsList = customerLevelRightsService.list(customerLevelRightsListReq);
        List<CustomerLevelRightsVO> newList = customerLevelRightsList.stream().map(entity -> customerLevelRightsService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new CustomerLevelRightsListResponse(newList));
    }

    @Override
    public BaseResponse<CustomerLevelRightsResponse> getById(@RequestBody @Valid CustomerLevelRightsQueryRequest customerLevelRightsByIdRequest) {
        CustomerLevelRights customerLevelRights = customerLevelRightsService.getById(customerLevelRightsByIdRequest.getRightsId());
        return BaseResponse.success(new CustomerLevelRightsResponse(customerLevelRightsService.wrapperVo(customerLevelRights)));
    }

}

