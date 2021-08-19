package com.wanmi.sbc.customer.bean.vo;

import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户详细信息
 * Created by CHENLI on 2017/4/13.
 */
@ApiModel
@Data
public class CustomerDetailSimplifyVO implements Serializable {

    private static final long serialVersionUID = -4567833252052862839L;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String customerName;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String contactPhone;

    /**
     * 负责业务员
     */
    @ApiModelProperty(value = "负责业务员")
    private String employeeId;

    /**
     * 账号状态 0：启用中  1：禁用中
     */
    @ApiModelProperty(value = "账号状态")
    private CustomerStatus customerStatus;

}
