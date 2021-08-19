package com.wanmi.sbc.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by zhangjin on 2017/4/23.
 */
@ApiModel
@Data
public class EmployeeResponse implements Serializable {

    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    private String employeeId;

    /**
     * 会员名字
     */
    @ApiModelProperty(value = "会员名字")
    private String employeeName;

    /**
     * 业务员手机号
     */
    @ApiModelProperty(value = "业务员手机号")
    private String employeeMobile;
}
