package com.wanmi.sbc.customer.provider.impl.growthvalue;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.growthvalue.CustomerGrowthValueQueryProvider;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValuePageRequest;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValueQueryRequest;
import com.wanmi.sbc.customer.api.response.growthvalue.CustomerGrowthValuePageResponse;
import com.wanmi.sbc.customer.api.response.growthvalue.CustomerGrowthValueTodayResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerGrowthValueVO;
import com.wanmi.sbc.customer.growthvalue.model.root.CustomerGrowthValue;
import com.wanmi.sbc.customer.growthvalue.service.CustomerGrowthValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>客户成长值明细表查询服务接口实现</p>
 *
 * @author yang
 * @since 2019/2/23
 */
@RestController
@Validated
public class CustomerGrowthValueQueryController implements CustomerGrowthValueQueryProvider {

    @Autowired
    private CustomerGrowthValueService customerGrowthValueService;

    @Override
    public BaseResponse<CustomerGrowthValuePageResponse> page(@RequestBody @Valid CustomerGrowthValuePageRequest request) {
        CustomerGrowthValueQueryRequest queryRequest = new CustomerGrowthValueQueryRequest();
        KsBeanUtil.copyPropertiesThird(request, queryRequest);
        Page<CustomerGrowthValue> page = customerGrowthValueService.page(queryRequest);
        Page<CustomerGrowthValueVO> newPage = page.map(entity -> customerGrowthValueService.wrapperVo(entity));
        MicroServicePage<CustomerGrowthValueVO> microPage = new MicroServicePage<>(newPage, request.getPageable());
        CustomerGrowthValuePageResponse finalRes = new CustomerGrowthValuePageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<CustomerGrowthValueTodayResponse> getGrowthValueToday(@RequestBody @Valid CustomerGrowthValueQueryRequest request) {
        Integer growthValue = customerGrowthValueService.getGrowthValueForMessage(request);
        return BaseResponse.success(CustomerGrowthValueTodayResponse.builder().growthValueSum(growthValue).build());
    }
}
