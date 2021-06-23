package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class CustomerIdListRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = -1585004539318321019L;

    @ApiModelProperty(value = "负责业务员")
    @NotNull
    private String employeeId;
}
