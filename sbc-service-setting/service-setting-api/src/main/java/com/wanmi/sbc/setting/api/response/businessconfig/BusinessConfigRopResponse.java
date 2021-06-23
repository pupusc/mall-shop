package com.wanmi.sbc.setting.api.response.businessconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by feitingting on 2019/11/21.
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessConfigRopResponse {
    /**
     * 招商页设置主键
     */
    @ApiModelProperty(value = "已新增的招商页设置信息")
    private Long businessConfigId;

    /**
     * 招商页banner
     */
    @ApiModelProperty(value = "已新增的招商页设置信息")
    private String businessBanner;

    /**
     * 招商页自定义
     */
    @ApiModelProperty(value = "已新增的招商页设置信息")
    private String businessCustom;

    /**
     * 招商页注册协议
     */
    @ApiModelProperty(value = "已新增的招商页设置信息")
    private String businessRegister;

    /**
     * 招商页入驻协议
     */
    @ApiModelProperty(value = "已新增的招商页设置信息")
    private String businessEnter;

    /**
     * 招商页banner
     */
    @ApiModelProperty(value = "商家的招商页设置信息")
    private String supplierBanner;

    /**
     * 招商页自定义
     */
    @ApiModelProperty(value = "商家的招商页设置信息")
    private String supplierCustom;

    /**
     * 招商页注册协议
     */
    @ApiModelProperty(value = "商家的招商页设置信息")
    private String supplierRegister;

    /**
     * 招商页入驻协议
     */
    @ApiModelProperty(value = "商家的招商页设置信息")
    private String supplierEnter;
}
