package com.wanmi.sbc.ares;

import com.wanmi.ares.provider.CustomerGrowthReportServiceProvider;
import com.wanmi.ares.request.customer.CustomerGrowthReportRequest;
import com.wanmi.ares.request.customer.CustomerTrendQueryRequest;
import com.wanmi.ares.view.customer.CustomerGrowthPageView;
import com.wanmi.ares.view.customer.CustomerGrowthReportView;
import com.wanmi.ares.view.customer.CustomerGrowthTrendView;
import com.wanmi.sbc.common.annotation.TransportMonitor;
import com.wanmi.sbc.common.base.BaseQueryResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.configure.ThriftClientConfig;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Objects.nonNull;

@Api(tags = "CustomerGrowthReportController", description = "客户增长统计")
@RestController
@RequestMapping("/customer_grow")
@Slf4j
@EnableConfigurationProperties(ThriftClientConfig.class)
public class CustomerGrowthReportController {

    @Autowired
    private CustomerGrowthReportServiceProvider customerGrowthReportServiceProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "客户增长列表统计")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @TransportMonitor
    public BaseResponse<BaseQueryResponse<CustomerGrowthReportView>> queryCustomerGrowthList(
            @RequestBody CustomerGrowthReportRequest reportRequest) {
        try {
            if (commonUtil.getOperator().getPlatform().equals(Platform.SUPPLIER)) {
                Long company = commonUtil.getCompanyInfoId();
                if (nonNull(company)) {
                    reportRequest.setCompanyId(company.toString());
                } else {
                    throw new SbcRuntimeException("K-000014");
                }
            } else {
                if (reportRequest.getCompanyId() == null || "".equals(reportRequest.getCompanyId())) {
                    reportRequest.setCompanyId("0");
                }
            }
            CustomerGrowthPageView customerGrowthPageView = customerGrowthReportServiceProvider.queryCustomerGrouthList(reportRequest);
            log.info("返回结果" + customerGrowthPageView);
            return BaseResponse.success(BaseQueryResponse.<CustomerGrowthReportView>builder()
                    .total(customerGrowthPageView.getTotal())
                    .pageSize(reportRequest.getPageSize())
                    .data(customerGrowthPageView.getGrouthList())
                    .pageNum(reportRequest.getPageNum())
                    .build());
        } catch (Exception e) {
            log.error("用户增长请求失败,", e);
            return BaseResponse.FAILED();

        }
    }

    @ApiOperation(value = "客户增长趋势统计")
    @RequestMapping(value = "/trend", method = RequestMethod.POST)
    @TransportMonitor
    public BaseResponse<List<CustomerGrowthTrendView>> queryCustomerTrendList(
            @RequestBody CustomerTrendQueryRequest request) {
        try {
            if (commonUtil.getOperator().getPlatform().equals(Platform.SUPPLIER)) {
                Long company = commonUtil.getCompanyInfoId();
                if (nonNull(company)) {
                    request.setCompanyInfoId(company.toString());
                } else {
                    throw new SbcRuntimeException("K-000014");
                }
            } else {
                if (request.getCompanyInfoId() == null || "".equals(request.getCompanyInfoId())) {
                    request.setCompanyInfoId("0");
                }
            }
            List<CustomerGrowthTrendView> list = customerGrowthReportServiceProvider.queryCustomerTrendList(request);
            return BaseResponse.success(list);
        } catch (Exception e) {
            log.error("用户增长请求失败,", e);
            return BaseResponse.FAILED();
        }
    }
}
