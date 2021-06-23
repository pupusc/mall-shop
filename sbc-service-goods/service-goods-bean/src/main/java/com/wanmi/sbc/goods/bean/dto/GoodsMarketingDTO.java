package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>商品营销</p>
 * author: sunkun
 * Date: 2018-11-02
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsMarketingDTO implements Serializable {

    private static final long serialVersionUID = 293845746515727190L;

    /**
     * 唯一编号
     */
    @ApiModelProperty(value = "唯一编号")
    private String id;

    /**
     * sku编号
     */
    @ApiModelProperty(value = "sku编号")
    private String goodsInfoId;

    /**
     * 客户编号
     */
    @ApiModelProperty(value = "客户编号")
    private String customerId;

    /**
     * 营销编号
     */
    @ApiModelProperty(value = "营销编号")
    private Long marketingId;


}

