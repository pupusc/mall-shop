package com.wanmi.sbc.elastic.provider.impl.employee;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.api.request.employee.EmployeePageRequest;
import com.wanmi.sbc.elastic.api.provider.employee.EsEmployeeQueryProvider;
import com.wanmi.sbc.elastic.api.request.employee.EsEmployeePageRequest;
import com.wanmi.sbc.elastic.api.response.employee.EsEmployeePageResponse;
import com.wanmi.sbc.elastic.bean.vo.customer.EsEmployeePageVO;
import com.wanmi.sbc.elastic.employee.mapper.EsEmployeeMapper;
import com.wanmi.sbc.elastic.employee.model.root.EsEmployee;
import com.wanmi.sbc.elastic.employee.service.EsEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class EsEmployeeQueryProviderImpl implements EsEmployeeQueryProvider {
    @Autowired
    EsEmployeeService esEmployeeService;

    @Autowired
    EsEmployeeMapper esEmployeeMapper;

    private static final Integer DEFAULT_SIZE = 2000;

    @Override
    public BaseResponse<EsEmployeePageResponse> page(@RequestBody @Valid EsEmployeePageRequest employeePageRequest) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = employeePageRequest.esCriteria();
        nativeSearchQueryBuilder.withPageable(employeePageRequest.getPageable());
        Page<EsEmployee> page = esEmployeeService.page(nativeSearchQueryBuilder.build());
        Page<EsEmployeePageVO> pageVo = page.map(esEmployee -> esEmployeeMapper.esEmployeeToEsEmployeePageVO(esEmployee));
        MicroServicePage<EsEmployeePageVO> employeePageVOPage = new MicroServicePage(pageVo,pageVo.getPageable());
        return BaseResponse.success(EsEmployeePageResponse.builder().employeePageVOPage(employeePageVOPage).build());
    }

    @Override
    public BaseResponse init(@RequestBody @Valid EsEmployeePageRequest employeePageRequest) {
        EmployeePageRequest pageRequest = esEmployeeMapper.esEmployeePageRequestToEmployeePageRequest(employeePageRequest);
        //设置为2000
        pageRequest.setPageSize(DEFAULT_SIZE);
        esEmployeeService.init(pageRequest);
        return BaseResponse.SUCCESSFUL();
    }
}
