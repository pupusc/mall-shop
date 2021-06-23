package com.wanmi.sbc.customer.bean.dto;

import com.wanmi.sbc.customer.bean.enums.CustomerType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerDetailFromEsDTO implements Serializable {

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


}
