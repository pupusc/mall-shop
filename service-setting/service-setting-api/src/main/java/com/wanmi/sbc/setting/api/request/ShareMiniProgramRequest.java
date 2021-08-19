package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分享商品生成携带分享人id的request
 */
@ApiModel
@Data
public class ShareMiniProgramRequest implements Serializable {

    private static final long serialVersionUID = 1L;
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


    @ApiModelProperty(value = "门店id")
    private Long storeId;

    @ApiModelProperty(value = "分享id")
    public String shareId;
}
