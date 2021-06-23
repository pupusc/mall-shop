package com.wanmi.sbc.setting.api.request.baseconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Created by feitingting on 2019/11/8.
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseConfigSaveRopRequest implements Serializable {
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Integer baseConfigId;

    /**
     * PC端商城网址
     */
    @ApiModelProperty(value = "PC端商城网址")
    @NotBlank
    private String pcWebsite;

    /**
     * 移动端商城网址
     */
    @ApiModelProperty(value = "移动端商城网址")
    @NotBlank
    private String mobileWebsite;

    /**
     * PC商城logo
     */
    @ApiModelProperty(value = "PC商城logo")
    private String pcLogo;

    /**
     * PC商城登录页banner,最多可添加5个,多个图片间以"|"隔开
     */
    @ApiModelProperty(value = "PC商城登录页banner,最多可添加5个,多个图片间以\"|\"隔开")
    private String pcBanner;

    /**
     * PC商城首页banner,最多可添加5个,多个图片间以"|"隔开
     */
    @ApiModelProperty(value = "PC商城首页banner,最多可添加5个,多个图片间以\"|\"隔开")
    private String pcMainBanner;

    /**
     * 移动商城banner,最多可添加5个,多个图片间以"|"隔开
     */
    @ApiModelProperty(value = "移动商城banner,最多可添加5个,多个图片间以\"|\"隔开")
    private String mobileBanner;

    /**
     * 商城图标，最多添加一个
     */
    @ApiModelProperty(value = "商城图标，最多添加一个")
    private String pcIco;

    /**
     * 商城首页标题
     */
    @ApiModelProperty(value = "商城首页标题")
    private String pcTitle;

    /**
     * 商家网址
     */
    @ApiModelProperty(value = "商家网址")
    private String supplierWebsite;

    /**
     * 会员注册协议
     */
    @ApiModelProperty(value = "会员注册协议")
    private String registerContent;
}
