package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 营销素材DTO
 */
@ApiModel
@Data
public class MarketingMaterialDTO{

    private static final long serialVersionUID = -8807674093765870168L;

    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private String imgSrc;

    /**
     * 配置的链接
     */
    @ApiModelProperty(value = "配置的链接")
    private String link;

    /**
     * 链接生成码的存放的redisKey
     */
    @ApiModelProperty(value = "链接生成码的存放的redisKey")
    private String redisKey;

    /**
     * 配置的链接生成的小程序码
     */
    @ApiModelProperty(value = "配置的链接生成的小程序码")
    private String linkSrc;
}
