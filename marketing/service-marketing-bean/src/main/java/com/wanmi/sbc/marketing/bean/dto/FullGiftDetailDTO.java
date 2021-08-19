package com.wanmi.sbc.marketing.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-20 14:03
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FullGiftDetailDTO implements Serializable {

    private static final long serialVersionUID = -874084946409580379L;
    /**
     *  满赠赠品Id
     */
    @ApiModelProperty(value = "满赠主键Id")
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
     *  营销id
     */
    @ApiModelProperty(value = "营销id")
    private Long marketingId;

}
