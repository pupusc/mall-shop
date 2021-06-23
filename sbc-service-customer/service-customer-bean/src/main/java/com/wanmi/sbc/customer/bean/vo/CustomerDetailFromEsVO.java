package com.wanmi.sbc.customer.bean.vo;

import com.wanmi.sbc.customer.bean.enums.CustomerType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerDetailFromEsVO implements Serializable {

    /**
     * 会员ID
     */
    private String customerId;


    /**
     * 客户等级ID
     */
    private Long customerLevelId;


    /**
     * 负责业务员
     */
    private String employeeId;

    /**
     * 客户等级名称
     */
    @ApiModelProperty(value = "客户等级名称")
    private String customerLevelName;

    /**
     * 成长值
     */
    @ApiModelProperty(value = "成长值")
    private Long growthValue;

    /**
     * 业务员名称
     */
    @ApiModelProperty(value = "业务员名称")
    private String employeeName;

    /**
     * 是否是我的客户（S2b-Supplier使用）
     */
    @ApiModelProperty(value = "是否是我的客户（S2b-Supplier使用")
    private Boolean myCustomer;

    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型")
    private CustomerType customerType;

}
