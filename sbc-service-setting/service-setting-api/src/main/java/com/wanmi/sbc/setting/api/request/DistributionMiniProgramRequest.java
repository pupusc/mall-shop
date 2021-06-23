package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class DistributionMiniProgramRequest implements Serializable {

    /**
     * 分销员会员ID
     */
    @ApiModelProperty(value = "分销员会员ID")
    private String inviteeId;

    /**
     * 分享人id
     */
    @ApiModelProperty(value = "分享人id")
    private String shareUserId;

    /**
     * 商品SkuId
     */
    @ApiModelProperty(value = "商品SkuId")
    private String skuId;

    /**
     * 商品SpuId
     */
    @ApiModelProperty(value = "商品SpuId")
    private String spuId;

    /**
     * 积分商品id
     */
    @ApiModelProperty(value = "积分商品ID")
    private String pointsGoodsId;

    /**
     * 渠道
     */
    @ApiModelProperty(value = "渠道,接受mall和shop传值")
    private String channel;


    /**
     * 邀新和店铺表示区分
     */
    @ApiModelProperty(value = "邀新和店铺表示区分,接受register和shop")
    private String tag;


    @ApiModelProperty(value = "门店id")
    private Long storeId;

    /**
     * 分享id
     */
    @ApiModelProperty(value = "分享id")
    public String shareId;
}
