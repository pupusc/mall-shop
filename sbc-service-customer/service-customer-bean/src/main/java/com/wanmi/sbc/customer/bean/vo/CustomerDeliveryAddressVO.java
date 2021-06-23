package com.wanmi.sbc.customer.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员收货地址-共用VO
 */
@ApiModel
@Data
public class CustomerDeliveryAddressVO implements Serializable {

    private static final long serialVersionUID = 3776508349285074170L;
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
    private String consigneeName;

    /**
     * 收货人手机号码
     */
    @ApiModelProperty(value = "收货人手机号码")
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
    @ApiModelProperty(value = "街道")
    private Long streetId;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String deliveryAddress;

    /**
     * 是否是默认地址 0：否 1：是
     */
    @ApiModelProperty(value = "是否是默认地址")
    private DefaultFlag isDefaltAddress;

    /**
     * 删除标志 0未删除 1已删除
     */
    @ApiModelProperty(value = "删除标志")
    private DeleteFlag delFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createPerson;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updatePerson;

    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime deleteTime;

    /**
     * 删除人
     */
    @ApiModelProperty(value = "删除人")
    private String deletePerson;

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private String provinceName;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String cityName;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private String areaName;

    /**
     * 街道
     */
    @ApiModelProperty(value = "街道")
    private String streetName;

    /**
     * 是否需要完善,true表示需要，false表示不需要
     */
    @ApiModelProperty(value = "是否需要完善,true表示需要，false表示不需要")
    private Boolean needComplete;
}
