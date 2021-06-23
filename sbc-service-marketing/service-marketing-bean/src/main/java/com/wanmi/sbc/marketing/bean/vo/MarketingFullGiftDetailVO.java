package com.wanmi.sbc.marketing.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 满赠
 */
@ApiModel
@Data
public class MarketingFullGiftDetailVO implements Serializable {

    private static final long serialVersionUID = -5305430465244477726L;

    /**
     *  满赠赠品Id
     */
    @ApiModelProperty(value = "满赠赠品主键Id")
    private Long giftDetailId;

    /**
     *  满赠多级促销Id
     */
    @ApiModelProperty(value = "满赠多级促销Id")
    private Long giftLevelId;

    /**
     *  赠品Id
     */
    @ApiModelProperty(value = "赠品Id")
    private String productId;

    /**
     *  赠品数量
     */
    @ApiModelProperty(value = "赠品数量")
    private Long productNum;

    /**
     *  满赠ID
     */
    @ApiModelProperty(value = "满赠营销ID")
    private Long marketingId;

}
