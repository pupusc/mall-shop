package com.wanmi.sbc.customer.address.request;

import com.wanmi.sbc.common.enums.DefaultFlag;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 客户配送地址
 * Created by CHENLI on 2017/4/20.
 */
@Data
public class CustomerDeliveryAddressEditRequest {
    /**
     * 收货地址ID
     */
    private String deliveryAddressId;
    /**
     * 客户ID
     */
    private String customerId;

    /**
     * 收货人
     */
    @NotBlank
    private String consigneeName;

    /**
     * 收货人手机号码
     */
    @NotBlank
    private String consigneeNumber;

    /**
     * 省
     */
    @NotNull
    private Long provinceId;

    /**
     * 市
     */
    private Long cityId;

    /**
     * 区
     */
    private Long areaId;

    /**
     * 街道
     */
    private Long streetId;

    /**
     * 详细地址
     */
    @NotBlank
    private String deliveryAddress;

    /**
     * 是否默认地址
     */
    private DefaultFlag isDefaltAddress = DefaultFlag.NO;
}
