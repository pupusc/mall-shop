package com.wanmi.sbc.customer.api.response.employee;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.EmployeePageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class EmployeePageResponse implements Serializable {

    private static final long serialVersionUID = -7143638713952743731L;

    @ApiModelProperty(value = "业务员列表")
    private MicroServicePage<EmployeePageVO> employeePageVOPage;
}
