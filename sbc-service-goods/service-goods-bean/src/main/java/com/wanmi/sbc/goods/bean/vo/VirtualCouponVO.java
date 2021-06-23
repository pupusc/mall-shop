package com.wanmi.sbc.goods.bean.vo;

import lombok.Data;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>卡券VO</p>
 *
 * @author 梁善
 * @date 2021-01-25 10:19:19
 */
@ApiModel
@Data
public class VirtualCouponVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 电子卡券ID
     */
    @ApiModelProperty(value = "电子卡券ID")
    private Long id;

    /**
     * 店铺标识
     */
    @ApiModelProperty(value = "店铺标识")
    private Long storeId;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 总数量
     */
    @ApiModelProperty(value = "总数量")
    private Integer sumNumber;

    /**
     * 可用数量
     */
    @ApiModelProperty(value = "可用数量")
    private Integer ableNumber;

    /**
     * 已售总数量
     */
    @ApiModelProperty(value = "已售总数量")
    private Integer saledNumber;

    /**
     * 已过期数量
     */
    @ApiModelProperty(value = "已过期数量")
    private Integer expireNumber;

    /**
     * 0:兑换码 1:券码+密钥 2:链接
     */
    @ApiModelProperty(value = "0:兑换码 1:券码+密钥 2:链接")
    private Integer provideType;

    /**
     * 0:未发布 1:已发布
     */
    @ApiModelProperty(value = "0:未发布 1:已发布")
    private Integer publishStatus;

    /**
     * 关联的skuId
     */
    @ApiModelProperty(value = "关联的skuId")
    private String skuId;

    /**
     * 关联的sku名称
     */
    @ApiModelProperty(value = "关联的sku名称")
    private String skuName;

    /**
     * 关联的sku编码
     */
    @ApiModelProperty(value = "关联的sku编码")
    private String skuNo;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String description;

}