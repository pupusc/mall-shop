package com.wanmi.sbc.elastic.employee.mapper;


import com.wanmi.sbc.customer.api.request.employee.EmployeePageRequest;
import com.wanmi.sbc.elastic.api.request.employee.EsEmployeePageRequest;
import com.wanmi.sbc.elastic.api.request.employee.EsEmployeeSaveRequest;
import com.wanmi.sbc.elastic.bean.vo.customer.EsEmployeePageVO;
import com.wanmi.sbc.elastic.employee.model.root.EsEmployee;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EsEmployeeMapper {

    @Mappings({})
    EsEmployeePageVO esEmployeeToEsEmployeePageVO(EsEmployee esEmployee);


    @Mappings({})
    EsEmployee esEmployeeAddRequestToEsEmployee(EsEmployeeSaveRequest esEmployeeSaveRequest);

    List<EsEmployee> esEmployeeAddRequestListToEsEmployeeList(List<EsEmployeeSaveRequest> esEmployeeSaveRequestList);

    @Mappings({})
    EmployeePageRequest esEmployeePageRequestToEmployeePageRequest(EsEmployeePageRequest employeePageRequest);



}
