package com.wanmi.sbc.customer.api.request.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeListByCompanyIdRequest implements Serializable {

    private static final long serialVersionUID = -4991736326698057033L;

    @ApiModelProperty(value = "商家id")
    @NotNull
    private Long companyInfoId;
}
