package com.wanmi.sbc.customer.api.request.employee;

import com.wanmi.sbc.customer.bean.enums.CustomerType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeActivateAccountRequest implements Serializable {
    private static final long serialVersionUID = -2871505287392325973L;

    /**
     * 员工id列表
     */
    @ApiModelProperty(value = "员工id列表")
    @NotNull
    private List<String> employeeIds;

    /**
     * 客户类型 0:平台客户,1:商家客户
     */
    @ApiModelProperty(value = "客户类型")
    private CustomerType customerType;

    /**
     * 所属商家Id
     */
    @ApiModelProperty(value = "所属商家Id")
    private Long companyInfoId;

    /**
     * 所属店铺Id
     */
    @ApiModelProperty(value = "所属店铺Id")
    private Long storeId;

    /**
     * 是否商家端
     */
    @ApiModelProperty(value = "是否商家端")
    private boolean s2bSupplier;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String operator;


}
