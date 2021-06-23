package com.wanmi.sbc.customer.bean.dto;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 会员收货地址-共用查询DTO
 */
@ApiModel
@Data
public class CustomerDeliveryAddressDTO implements Serializable {


    private static final long serialVersionUID = 1314012542242540721L;
    /**
     * 收货地址ID
     */
    @ApiModelProperty(value = "收货地址ID")
    private String deliveryAddressId;
    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    private String customerId;

    /**
     * 收货人
     */
    @ApiModelProperty(value = "收货人")
    @NotBlank
    private String consigneeName;

    /**
     * 收货人手机号码
     */
    @ApiModelProperty(value = "收货人手机号码")
    @NotBlank
    private String consigneeNumber;

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private Long provinceId;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private Long cityId;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private Long areaId;

    /**
     * 街道
     */
    private Long streetId;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    @NotBlank
    private String deliveryAddress;

    /**
     * 是否默认地址
     */
    @ApiModelProperty(value = "是否默认地址")
    private DefaultFlag isDefaltAddress = DefaultFlag.NO;
}
