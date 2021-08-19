package com.wanmi.sbc.ares;

import com.wanmi.ares.provider.EmployeeQueryServiceProvider;
import com.wanmi.ares.request.employee.EmployeeClientQueryRequest;
import com.wanmi.ares.request.employee.EmployeePerformanceQueryRequest;
import com.wanmi.ares.view.employee.EmployeeClientResponse;
import com.wanmi.ares.view.employee.EmployeePerormanceResponse;
import com.wanmi.sbc.aop.EmployeeCheck;
import com.wanmi.sbc.common.base.BaseResponse;
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

import javax.annotation.Resource;

import static java.util.Objects.isNull;

/**
 * <p>业务员报表查询</p>
 * Created by of628-wenzhi on 2017-10-20-下午9:23.
 */
@Api(tags = "EmployeeReportController", description = "业务员报表查询")
@RestController
@RequestMapping("/view/employee/")
@Slf4j
@EnableConfigurationProperties(ThriftClientConfig.class)
public class EmployeeReportController {
//    @Resource
//    private ObjectPool<TProtocol> tProtocolObjectPool;

    @Autowired
    private EmployeeQueryServiceProvider employeeQueryServiceProvider;

    @Resource
    private CommonUtil commonUtil;

    @ApiOperation(value = "业务员客户端视图")
    @EmployeeCheck
    @RequestMapping(value = "/client", method = RequestMethod.POST)
    @SuppressWarnings("unchecked")
    public BaseResponse<EmployeeClientResponse> clientView(@RequestBody EmployeeClientQueryRequest request) {
        try {
            if (request.getCompanyId() == null) {
                Long companyInfoId = commonUtil.getCompanyInfoId();
                request.setCompanyId(isNull(companyInfoId) ? "0" : companyInfoId.toString());
            }
            EmployeeClientResponse response = employeeQueryServiceProvider.queryViewByClient(request);
            return BaseResponse.success(response);
        } catch (Exception e) {
            log.error("Get employee client view fail,the thrift request error,", e);
            return BaseResponse.FAILED();
        }
    }

    @ApiOperation(value = "业务员绩效视图")
    @RequestMapping(value = "/performance", method = RequestMethod.POST)
    @SuppressWarnings("unchecked")
    public BaseResponse<EmployeePerormanceResponse> performanceView(
            @RequestBody EmployeePerformanceQueryRequest request) {
        try {
            if (request.getCompanyId() == null) {
                Long companyInfoId = commonUtil.getCompanyInfoId();
                request.setCompanyId(isNull(companyInfoId) ? "0" : companyInfoId.toString());
            }
            EmployeePerormanceResponse response = employeeQueryServiceProvider.queryViewByPerformance(request);
            return BaseResponse.success(response);
        } catch (Exception e) {
            log.error("Get employee performance view fail,the thrift request error,", e);
            return BaseResponse.FAILED();
        }
    }
}
