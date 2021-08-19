package com.wanmi.sbc.customer.provider.impl.levelrights;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.levelrights.CustomerLevelRightsRelQueryProvider;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsRelRequest;
import com.wanmi.sbc.customer.api.response.levelrights.CustomerLevelRightsRelListResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelRightsRelVO;
import com.wanmi.sbc.customer.levelrights.model.root.CustomerLevelRightsRel;
import com.wanmi.sbc.customer.levelrights.service.CustomerLevelRightsRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>会员等级与权益关联表查询服务</p>
 *
 * @author yang
 * @since 2019/2/27
 */
@RestController
@Validated
public class CustomerLevelRightsRelQueryController implements CustomerLevelRightsRelQueryProvider {

    @Autowired
    private CustomerLevelRightsRelService customerLevelRightsRelService;

    @Override
    public BaseResponse<CustomerLevelRightsRelListResponse> listByRightsId(@RequestBody @Valid CustomerLevelRightsRelRequest customerLevelRightsRelQueryRequest) {
        List<CustomerLevelRightsRel> customerLevelRightsRelList = customerLevelRightsRelService.listByRightsId(customerLevelRightsRelQueryRequest);
        List<CustomerLevelRightsRelVO> newList = customerLevelRightsRelList.stream().map(entity -> customerLevelRightsRelService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new CustomerLevelRightsRelListResponse(newList));
    }
}
