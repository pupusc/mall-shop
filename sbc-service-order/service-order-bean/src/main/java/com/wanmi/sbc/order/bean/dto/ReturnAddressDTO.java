package com.wanmi.sbc.order.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author：zhangwenchang
 * @Date：2020/11/4 09:48
 * @Description：退货地址
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnAddressDTO implements Serializable {

    /**
     * addressId Id
     */
    @ApiModelProperty(value = "addressId Id")
    private String id;

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
    @ApiModelProperty(value = "街道")
    private Long streetId;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String address;

    /***
     * 详细地址(包含省市区）
     */
    @ApiModelProperty(value = "详细地址(包含省市区）")
    private String detailAddress;

    /**
     * 收货人名称
     */
    @ApiModelProperty(value = "收货人名称")
    private String name;

    /**
     * 收货人电话
     */
    @ApiModelProperty(value = "收货人电话")
    private String phone;

}
