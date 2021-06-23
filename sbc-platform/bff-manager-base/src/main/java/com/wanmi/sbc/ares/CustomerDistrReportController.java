package com.wanmi.sbc.ares;

import com.wanmi.ares.provider.CustomerDistrQueryServiceProvider;
import com.wanmi.ares.request.customer.CustomerDistrQueryRequest;
import com.wanmi.ares.view.customer.CustomerAreaDistrResponse;
import com.wanmi.ares.view.customer.CustomerLevelDistrResponse;
import com.wanmi.sbc.common.base.BaseResponse;
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

import static java.util.Objects.isNull;

/**
 * <p>客户分布统计报表Controller</p>
 * Created by of628-wenzhi on 2017-10-18-下午2:16.
 */
@Api(tags = "CustomerDistrReportController", description = "客户分布统计报表 Api")
@RestController
@RequestMapping("/view/customer/distribute/")
@Slf4j
@EnableConfigurationProperties(ThriftClientConfig.class)
public class CustomerDistrReportController {

    @Autowired
    private CustomerDistrQueryServiceProvider customerDistrQueryServiceProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "客户级别分布统计")
    @RequestMapping(value = "/level", method = RequestMethod.POST)
    @SuppressWarnings("unchecked")
    public BaseResponse<CustomerLevelDistrResponse> levelView(@RequestBody CustomerDistrQueryRequest request) {
        try {
            if (request.getCompanyId() == null) {
                Long companyInfoId = commonUtil.getCompanyInfoId();
                if (isNull(companyInfoId)) {
                    //平台端不提供客户级别分布统计
                    throw new SbcRuntimeException("K-000003");
                }
                request.setCompanyId(companyInfoId.toString());
            }
            CustomerLevelDistrResponse response = customerDistrQueryServiceProvider.queryLevelDistrView(request);
            return BaseResponse.success(response);
        } catch (Exception e) {
            log.error("Get customer level distribute view fail,the thrift request error,", e);
            return BaseResponse.FAILED();
        }
    }

    @ApiOperation(value = "客户区域统计")
    @RequestMapping(value = "/area", method = RequestMethod.POST)
    @SuppressWarnings("unchecked")
    public BaseResponse<CustomerAreaDistrResponse> areaView(@RequestBody CustomerDistrQueryRequest request) {
        try {
            if (request.getCompanyId() == null) {
                Long companyInfoId = commonUtil.getCompanyInfoId();
                request.setCompanyId(isNull(companyInfoId) ? "0" : companyInfoId.toString());
            }
            CustomerAreaDistrResponse response = customerDistrQueryServiceProvider.queryAreaDistrView(request);
            return BaseResponse.success(response);
        } catch (Exception e) {
            log.error("客户区域统计,", e);
            return BaseResponse.FAILED();
        }
    }

    @ApiOperation(value = "客户总数统计")
    @RequestMapping
    @SuppressWarnings("unchecked")
    public BaseResponse<Integer> total(@RequestBody CustomerDistrQueryRequest request) {
        try {
            if (request.getCompanyId() == null) {
                Long companyInfoId = commonUtil.getCompanyInfoId();
                request.setCompanyId(isNull(companyInfoId) ? "0" : companyInfoId.toString());
            }
            int count = customerDistrQueryServiceProvider.totalCount(request);
            return BaseResponse.success(count);
        } catch (Exception e) {
            log.error("客户总数统计,", e);
            return BaseResponse.FAILED();
        }
    }
}
