package com.wanmi.sbc.ares;

import com.wanmi.ares.provider.CustomerReportQueryServiceProvider;
import com.wanmi.ares.request.customer.CustomerOrderQueryRequest;
import com.wanmi.ares.view.customer.CustomerOrderPageView;
import com.wanmi.ares.view.customer.CustomerOrderView;
import com.wanmi.sbc.common.base.BaseQueryResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Objects.nonNull;

@Api(tags = "CustomerReportController", description = "客户报表统计")
@RestController
@RequestMapping("/customer_report")
@Slf4j
public class CustomerReportController {

    @Autowired
    private CustomerReportQueryServiceProvider customerReportQueryServiceProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "客户订单统计")
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @SuppressWarnings("uncheced")
    public BaseResponse<BaseQueryResponse<CustomerOrderView>> queryCustomerOrder(@RequestBody CustomerOrderQueryRequest request) throws Exception {
        try {
            if (commonUtil.getOperator().getPlatform().equals(Platform.SUPPLIER)) {
                Long company = commonUtil.getCompanyInfoId();
                if (nonNull(company)) {
                    request.setCompanyId(company.toString());
                } else {
                    throw new SbcRuntimeException("K-000014");
                }
            } else {
                if (StringUtils.isEmpty(request.getCompanyId()) || "".equals(request.getCompanyId())) {
                    request.setCompanyId("0");
                }
            }
            CustomerOrderPageView customerOrderPageView = customerReportQueryServiceProvider.queryCustomerOrders(request);
            BaseResponse response = BaseResponse.success(BaseQueryResponse
                    .<CustomerOrderView>builder()
                    .pageSize(request.getPageSize())
                    .total(customerOrderPageView.getTotal())
                    .pageNum(request.getPageNum())
                    .data(customerOrderPageView.getCustomerOrderViewList()).build());
            log.info(response.toString());
            return response;
        } catch (Exception e) {
            log.error("查询会员订货报表失败,", e);
            return BaseResponse.FAILED();
        }
    }

}
