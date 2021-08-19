package com.wanmi.sbc.customer.bean.vo;

import com.wanmi.sbc.customer.bean.enums.CustomerType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 店铺-会员(包含会员等级)关联实体类
 * Created by bail on 2017/11/13.
 */
@ApiModel
@Data
public class StoreCustomerRelaVO implements Serializable {

    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 用户标识
     */
    @ApiModelProperty(value = "用户标识")
    private String customerId;

    /**
     * 店铺标识
     */
    @ApiModelProperty(value = "店铺标识")
    private Long storeId;

    /**
     * 商家标识
     */
    @ApiModelProperty(value = "商家标识")
    private Long companyInfoId;

    /**
     * 店铺等级标识
     */
    @ApiModelProperty(value = "店铺等级标识")
    private Long storeLevelId;

    /**
     * 负责的业务员标识
     */
    @ApiModelProperty(value = "负责的业务员标识")
    private String employeeId;

    /**
     * 关系类型(0:店铺关联的客户,1:店铺发展的客户)
     */
    @ApiModelProperty(value = "关系类型")
    private CustomerType customerType;
}

