package com.wanmi.sbc.order.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 收货人信息
 * Created by jinwei on 19/03/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class ConsigneeVO implements Serializable {

    /**
     * CustomerDeliveredAddress Id
     */
    @ApiModelProperty(value = "id")
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

    /**
     * 期望收货时间
     */
    @ApiModelProperty(value = "期望收货时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime expectTime;

    /**
     * 收货地址修改时间
     */
    @ApiModelProperty(value = "收货地址修改时间")
    private String updateTime;

    /**
     * 省名称（老订单使用）
     */
    @ApiModelProperty(value = "省名称（老订单使用）")
    private String provinceName;

    /**
     * 市名称（老订单使用）
     */
    @ApiModelProperty(value = "市名称（老订单使用）")
    private String cityName;

    /**
     * 区名称（老订单使用）
     */
    @ApiModelProperty(value = "区名称（老订单使用）")
    private String areaName;
}
