package com.wanmi.sbc.elastic.api.provider.employee;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.employee.EsEmployeePageRequest;
import com.wanmi.sbc.elastic.api.response.employee.EsEmployeePageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "${application.elastic.name}", contextId = "EsEmployeeQueryProvider")
public interface EsEmployeeQueryProvider {

    /**
     * Es分页查询
     *
     * @param employeePageRequest {@link EsEmployeePageRequest}
     * @return 员工列表信息（带分页）{@link EsEmployeePageResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/esEmployee/page")
    BaseResponse<EsEmployeePageResponse> page(@RequestBody @Valid EsEmployeePageRequest employeePageRequest);

    /**
     * Es init数据
     *
     * @param employeePageRequest {@link EsEmployeePageRequest}
     * @return 员工列表信息（带分页）{@link EsEmployeePageResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/esEmployee/init")
    BaseResponse init(@RequestBody @Valid EsEmployeePageRequest employeePageRequest);

}
