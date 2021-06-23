package com.wanmi.sbc.order.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: lq
 * @CreateTime:2019-05-18 14:32
 * @Description:todo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrouponValidateDTO {
    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private String customerId;


    /**
     * 商品skuId
     */
    @ApiModelProperty("商品skuId")
    private String goodsInfoId;

    /**
     * 购买数量
     */
    @ApiModelProperty("购买数量")
    private Integer buyCount;

    /**
     * 是否开团购买(true:开团 false:参团 null:非拼团购买)
     */
    @ApiModelProperty("是否开团购买")
    private Boolean openGroupon;

    /**
     * 团号
     */
    @ApiModelProperty("团号")
    private String grouponNo;


}
